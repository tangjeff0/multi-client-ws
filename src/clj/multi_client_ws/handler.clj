(ns multi-client-ws.handler
  (:require
    [multi-client-ws.middleware :as middleware]
    [multi-client-ws.layout :refer [error-page]]
    [multi-client-ws.routes.home :refer [home-routes]]
    [reitit.ring :as ring]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.middleware.webjars :refer [wrap-webjars]]
    [multi-client-ws.env :refer [defaults]]
    [multi-client-ws.routes.websockets :refer [websocket-routes]]
    [mount.core :as mount]))

(mount/start)
(mount/stop)

(mount/defstate init-app
  :start ((or (:init defaults) (fn [])))
  :stop  ((or (:stop defaults) (fn []))))

#_(count [(home-routes)
          websocket-routes])

(mount/defstate app-routes
  :start
  (ring/ring-handler
    (ring/router
      [(home-routes)
       websocket-routes])
    (ring/routes
      (ring/create-resource-handler
        {:path "/"})

      (wrap-content-type
        (wrap-webjars (constantly nil)))
      (ring/create-default-handler
        {:not-found
         (constantly (error-page {:status 404, :title "404 - Page not found"}))
         :method-not-allowed
         (constantly (error-page {:status 405, :title "405 - Not allowed"}))
         :not-acceptable
         (constantly (error-page {:status 406, :title "406 - Not acceptable"}))}))))

(defn app []
  (middleware/wrap-base #'app-routes))
