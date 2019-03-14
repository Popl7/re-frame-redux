(ns re-frame-redux.core
  (:require [re-frame.core :as re-frame]
            [cljs.spec.alpha :as s]
            [cljs.reader :refer [read-string]]))


;; Dispatchers
;;
(def with-dev-tools
  (try
    (and
      (exists? js/window)
      (.-__REDUX_DEVTOOLS_EXTENSION__ js/window))
    (catch :default _
      false)))

(defn dispatch
  "Dispatches state snapshot to Chrome Devtools toolbar"
  [action state]
  (when ^boolean js/goog.DEBUG
    (when-let [devTools with-dev-tools]
      (.send devTools (clj->js action) (clj->js state)))))

(def redux-debug
  (re-frame/->interceptor
    :id    :redux-debug
    :after (fn [{:keys [coeffects effects] :as context}]
             (let [[name & args] (:event coeffects)
                   state         (or (:db effects)
                                     (:db coeffects))
                   state         (assoc state :cljs (pr-str state))]
               (let [action {:type (str name)}
                     action (if (map? args)
                                (merge action args)
                                (assoc action :values args))]
                 (dispatch action state)))
             context)))


;; Interceptors
;;
(def standard-interceptors
  [redux-debug])

(defn reg-event-db
  "Example event-db registration with added redux interceptor"
  ([id handler-fn]
   (re-frame/reg-event-db id standard-interceptors handler-fn))
  ([id interceptors handler-fn]
   (re-frame/reg-event-db id [standard-interceptors interceptors] handler-fn)))

(defn reg-event-fx
  "Example event-fx registration with added redux interceptor"
  ([id handler-fn]
   (re-frame/reg-event-fx id standard-interceptors handler-fn))
  ([id interceptors handler-fn]
   (re-frame/reg-event-fx id [standard-interceptors interceptors] handler-fn)))


;; Initialization
;;
(defn register-redux-events
  "Register load-db re-frame handler for time travelling.
   Takes as optional argument a cljs.spec which will validate the database.
   Call this where you register your re-frame event handlers."
  ([]
   (re-frame/reg-event-db
     :load-db
     (fn [_ [_ new-db]]
       (.warn js/console "[Redux]: setting db")
       new-db)))

  ([db-spec]
   (re-frame/reg-event-db
     :load-db
     (fn [db [_ new-db]]
       (if (s/valid? db-spec new-db)
         (do
           (.warn js/console "[Redux]: setting db")
           new-db)
         (do
           (.error js/console "[Redux]: NOT setting db, invalid: " (s/explain-data db-spec new-db))
           db))))))

(defn setup
  "Attach the Chrome Devtools toolbar to re-re-frame.
  Call this in your client init function."
  []
  (when-let [devTools with-dev-tools]
    (.subscribe (.connect devTools)
                (fn [msg]
                  (let [msg (js->clj msg :keywordize-keys true)]
                    (when (and (= (:type msg) "DISPATCH")
                               (= (get-in msg [:payload :type]) "JUMP_TO_STATE"))
                      (let [new-state (-> (:state msg)
                                          (js/JSON.parse)
                                          (js->clj :keywordize-keys true)
                                          (:cljs)
                                          (read-string))]
                        (re-frame/dispatch [:load-db new-state]))))))))
