(ns multi-client-ws.routes.websockets
  (:require
    [clojure.tools.logging :as log]
    [immutant.web.async :as async]
    [datahike.api :as d]))


(def cfg {:store              {:backend :file
                               :path    "/tmp/datahike"}
          :schema-flexibility :write
          :keep-history?      true})

(d/create-database cfg)
(def conn (d/connect cfg))



(defonce channels (atom #{}))

(defn connect! [channel]
  (log/info "channel open")
  (swap! channels conj channel))

(defn disconnect! [channel {:keys [code reason]}]
  (log/info "close code:" code "reason:" reason)
  (swap! channels #(remove #{channel} %)))

(defn notify-clients! [channel msg]
  (doseq [channel @channels]
    (async/send! channel msg)))

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open connect!
   :on-close disconnect!
   :on-message notify-clients!})

(defn ws-handler [request]
  (async/as-channel request websocket-callbacks))

(def websocket-routes
  [["/ws" ws-handler]])
