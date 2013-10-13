(ns pallet.task.bash
  "Execute bash commands on nodes"
  (:require
   [clojure.pprint :refer [pprint]]
   [clojure.stacktrace :refer [print-cause-trace]]
   [clojure.string :refer [trim]]
   [clojure.tools.logging :refer [tracef]]
   [doric.core :refer [table]]
   [pallet.actions :refer [exec-script]]
   [pallet.api :as api]
   [pallet.compute :as compute]
   [pallet.core.api :refer [phase-errors service-groups]]
   [pallet.node :refer [group-name node-map primary-ip]]
   [pallet.task-utils :refer [pallet-project process-args project-groups]]
   [pallet.utils :refer [apply-map]]))

(def switches
  [["-s" "--selectors" "A comma separated list of selectors"
    :default "default"]
   ["-g" "--groups" "A comma separated list of groups"]
   ["-r" "--roles" "A comma separated list of group roles"]
   ["-o" "--show-output" "Show command output" :flag true :default false]])

(def help
  (str "Execute arbitrary commands on nodes."
       \newline \newline
       (last (process-args "bash" nil switches))))

(defn lift-groups
  [compute groups args options]
  (apply-map api/lift groups :phase (api/plan-fn (exec-script ~@args)) options))

(defn process-result
  [op show-output]
  (if (phase-errors op)
      (binding [*out* *err*]
        (println "An error occured")
        (when-let [e (seq (phase-errors op))]
          (println (table (->> e (map :error) (map #(dissoc % :type)))))
          (doseq [f e
                  :let [ex (-> f :error :exception)]
                  :when ex]
            (print-cause-trace ex)
            (throw (ex-info "pallet bash failed" {:exit-code 1} ex))))
        (when-let [e (:exception op)]
          (print-cause-trace e)
          (throw (ex-info "pallet bash failed" {:exit-code 1} e)))
        (throw (ex-info "See logs for further details" {:exit-code 1})))
      (when show-output
        (println (table
                  (->> (:results op)
                       (mapcat (fn [r] (map #(merge r %) (:result r))))
                       (map #(hash-map
                              :ip (primary-ip (-> % :target :node))
                              :out (trim (:out %))))))))))

(defn bash
  {:doc help}
  [{:keys [compute project] :as request} & args]
  (tracef "bash %s" (vec args))
  (let [[{:keys [selectors quiet groups roles show-output]} args doc]
        (process-args "bash <script command line>" args switches)
        ;; project (pallet-project project)
        ;; spec (when project
        ;;        (project-groups project compute selectors groups roles))
        groups (or ;; spec
                (service-groups compute))]
    (tracef "bash running on %s groups" (count groups))
    (if (seq args)
      (-> (lift-groups compute
                       groups
                       args
                       (->
                        request
                        (dissoc :config :project)
                        (assoc :environment
                          (or (:environment request)
                              (-> request :project :environment)))))
          (process-result show-output))
      (println doc))))
