# pallet-cli

A command line for pallet.  Not ready for use!

## Install

Pallet bootstraps itself using the `pallet` shell script; there is no
separate install script. It handles installing its own dependencies,
which means the first run will take longer.

1. Make sure you have a Java JDK version 6 or later.
2. [Download the script](https://raw.github.com/pallet/pallet-cli/develop/bin/pallet).
3. Place it on your `$PATH`. (`~/bin` is a good choice if it is on your path.)
4. Set it to be executable. (`chmod 755 ~/bin/pallet`)

It is also recommended to
[download `grench`](http://leiningen.org/grench.html), which will be
used if available, and makes command line usage much snappier.

## Usage

By default, pallet's node-list provider is used.  To get started, add
your nodes to `~/.pallet/nodes.edn`, eg:

```clj
{:localhost ["127.0.0.1"]}
```

Try listing the nodes:

```
pallet nodes
```

### Control the Pallet Server Process

When using `grench` a server process is launched.  To kill the server process:

    pallet server kill

## License

Copyright © 2013 Hugo Duncan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
