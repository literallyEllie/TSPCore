## TSPCore
~~Core structure for TSP Network~~

This plugin was originally for a client however there were some issues so I am posting it publically.

Whilst this plugin is not complete, it features the basis of a core plugin with plans to integrate MongoDB as the storage medium 
and Redis as the cache.  

### Feature list:
* Command wrapper + handle
* Redis handle + PubSub wrapper 
* Mongo handle
* Account structure
* Untested basis for account syncing
* Basis for a rank (explicit permission substitute) hierarchy
* An auto-generated server config handling how the server will run
* 2 lazily made commands
* Allowing for other plugins to be built on it, extending TSPPlugin
* Preset for some server messages, made lazily.
* Some util methods

### Forking
If you want to expand upon this feel free.

Have a good day.