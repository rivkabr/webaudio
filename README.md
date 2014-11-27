webaudio
=======

`webaudio` is a clojurescript interface to part of the HTML5 Audio API.

It provides also many more functions to access the API:

* `decode-data` for `ogg` file through the native API
* `decode-data` for `flac` file through [aurora](https://github.com/audiocogs/aurora.js)
* `set-volume-speakers`
* `pcm->buffer`
* `stereo->4chan`


Usage
-----
Add to your `project.clj`

[![Clojars Project](http://clojars.org/viebel/webaudio/latest-version.svg)](http://clojars.org/viebel/webaudio)

and in addition you have to add `aurora.js` to your project.
You can download it from: `https://github.com/viebel/webaudio/blob/master/resources/aurora.js`.

See [here](https://groups.google.com/forum/#!searchin/clojurescript/javascript$20extern/clojurescript/iBWLAJ3TW7I/GKhvWnzFlNEJ) why this is required for the moment.


Deployment (to [clojars](https://clojars.org/))
------------------------------------
Update the version number in `project.clj` and then execute:

```
lein deploy clojars
```
