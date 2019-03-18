# re-frame-redux

A ClojureScript libraryto couple the re-frame database to the redux devtools.
Works in 2 directions, e.g. with time travelling.

[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.unrealistic/re-frame-redux.svg)](https://clojars.org/org.clojars.unrealistic/re-frame-redux)

## Demo
[Youtube Demo](https://youtu.be/qsF0pQKrnGM)

[Bamse example project using it](https://gitlab.com/StevenT/bamse.git)

## Usage
This library can be hooked into a re-frame application.

### In core.cljs
Require the library: [re-frame-redux.core :as redux]
(redux/setup)

### In events.cljs
Require the library: [re-frame-redux.core :as redux]
Where you register your events add: (redux/register-redux-events).
And register your event handlers with redux/reg-event-db and redux/reg-event-fx.

### Checking with spec
Optional you can register the redux-event interceptor with a cljs.spec to validate the database on changes: (redux/register-redux-events :my-spec/my-db-spec).

## License
MIT License

Copyright (c) 2019 Steven Thonus
