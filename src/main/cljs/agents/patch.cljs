(ns agents.patch
  (:require [lib.entity :refer [entity*
                                entity-init]]
            [agents.comps :refer [indexed
                                  self-vars
                                  patch-coordinates
                                  turtle-set
                                  turtle-getters
                                  turtles-at
                                  turtle-tracking
                                  sprout-turtles
                                  patch-topology
                                  ask
                                  watch
                                  compare-by-id
                                  patch-to-string
                                  patch-reset
                                  cl-update]])
  (:require-macros [lib.component :refer [compnt-let]]))

;; Persisting issues -- JTT (8/29/14)
;;
;; * Can't pass a set of agents.patch-s to in-radius
;;              (or to anything else taking an agentset)
;; * Aliasing -> patchset of basically the same thing
;;

(def PATCH_NAME "patch")
;; REFER: poorly spliced update mapping in
;; cl-shiv/updater-hackpatch-cl.js -- 8/29/14

(def patch-defaults
  {:pcolor 0
   :plabel ""
   :plabel-color 9.9})

(defn- _patch_entity [x y]
  (entity* :patch
           :init [(indexed :patch)
                  (self-vars patch-defaults)
                  (patch-coordinates x y)
                  (turtle-set)
                  (turtle-getters)
                  (turtles-at)
                  (turtle-tracking)
                  (sprout-turtles)
                  (patch-topology)
                  (ask)
                  (watch)
                  (compare-by-id)
                  (patch-to-string)
                  (patch-reset)
                  (cl-update)
                  (patch-aliases)]))

(defn patch [x y]
  (entity-init (_patch_entity x y)))

(defn js-patch [x y]
  (clj->js (patch x y)))

(compnt-let patch-aliases []

            [get-var :get-var
             set-var! :set-var!

             track-turtle :track-turtle
             untrack-turtle :untrack-turtle

             get-coords :get-coords

             distance-xy :distance-xy
             towards-xy :towards-xy
             get-neighbors :get-neighbors
             get-neighbors-4 :get-neighbors-4
             in-radius :in-radius

             turtles-at :turtles-at
             turtles-here :turtles-here
             breed-here :breed-here

             projection-by :projection-by

             to-string :to-string]

            :getVariable get-var
            :setVariable set-var!

            :getPatchVariable get-var
            :setPatchVariable set-var!

            :untrackTurtle untrack-turtle
            :trackTurtle track-turtle

            :getCoords get-coords
            :distanceXY distance-xy
            :towardsXY towards-xy
            :getNeighbors get-neighbors
            :getNeighbors4 get-neighbors-4
            :inRadius in-radius

            :turtlesAt turtles-at
            :turtlesHere turtles-here
            :breedHere breed-here

            :projectionBy projection-by

            :toString to-string)
