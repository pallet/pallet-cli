(ns pallet.task.server
  "Control the pallet server process."
  (:require
   [pallet.task-utils :refer [process-args]])
  (:import
   java.lang.management.ManagementFactory))

(def switches
  [])

(def help
  (str "Control the pallet server.\n"
       \newline
       "server [kill|pid]" \newline \newline))

(defn ^String pid
  "Return the pid of the current process."
  []
  (second (re-find #"(\d+)@.*"
                   (.. (ManagementFactory/getRuntimeMXBean) getName))))

(defn server
  {:doc help
   :no-service-required true}
  [{:keys [compute project] :as request} & args]
  (let [[{:keys [selectors quiet groups roles]} [cmd & args]]
        (process-args "down" args switches)]
    (cond
     (= cmd "kill") (do (shutdown-agents)
                        (System/exit 0))
     (= cmd "pid") (println (pid))
     :else (do
             (when cmd
               (println "Unknown command" cmd \newline))
             (println help)))))
