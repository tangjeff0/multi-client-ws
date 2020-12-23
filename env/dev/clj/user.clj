(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [multi-client-ws.config :refer [env]]
    [clojure.pprint]
    [clojure.spec.alpha :as s]
    [expound.alpha :as expound]
    [mount.core :as mount]
    [multi-client-ws.figwheel :refer [start-fw stop-fw cljs]]
    [multi-client-ws.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn start
  "Starts application.
  You'll usually want to run this on startup."
  []
  (mount/start-without #'multi-client-ws.core/repl-server))

(defn stop
  "Stops application."
  []
  (mount/stop-except #'multi-client-ws.core/repl-server))

(defn restart
  "Restarts application."
  []
  (stop)
  (start))


