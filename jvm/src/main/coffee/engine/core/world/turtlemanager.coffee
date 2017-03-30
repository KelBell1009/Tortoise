# (C) Uri Wilensky. https://github.com/NetLogo/Tortoise

ColorModel = require('engine/core/colormodel')
Nobody     = require('../nobody')
Turtle     = require('../turtle')
TurtleSet  = require('../turtleset')
Builtins   = require('../structure/builtins')
IDManager  = require('./idmanager')

{ map }        = require('brazierjs/array')
{ rangeUntil } = require('brazierjs/number')

{ DeathInterrupt, ignoring }  = require('util/exception')

module.exports =
  class TurtleManager

    _idManager:   undefined # IDManager
    _turtles:     undefined # Array[Turtle]
    _turtlesById: undefined # Object[Number, Turtle]

    # (World, Updater, BreedManager, (Number) => Number) => TurtleManager
    constructor: (@_world, @_breedManager, @_updater, @_nextInt) ->
      @_idManager   = new IDManager
      @_turtles     = []
      @_turtlesById = {}

    # () => Unit
    clearTurtles: ->
      @turtles().forEach((turtle) -> ignoring(DeathInterrupt)(() => turtle.die()))
      @_idManager.reset()
      return

    # (Number, String) => TurtleSet
    createOrderedTurtles: (n, breedName) ->
      num     = if n >= 0 then n else 0
      turtles = map(
        (index) =>
          color   = ColorModel.nthColor(index)
          heading = (360 * index) / num
          @_createTurtle(@_idManager.next(), color, heading, 0, 0, @_breedManager.get(breedName))
      )(rangeUntil(0)(num))
      new TurtleSet(turtles)

    # (Number, String, Number, Number) => TurtleSet
    createTurtles: (n, breedName, xcor = 0, ycor = 0) ->
      num     = if n >= 0 then n else 0
      turtles = map(=>
        color   = ColorModel.randomColor(@_nextInt)
        heading = @_nextInt(360)
        @_createTurtle(@_idManager.next(), color, heading, xcor, ycor, @_breedManager.get(breedName))
      )(rangeUntil(0)(num))
      new TurtleSet(turtles)

    # (Number) => Agent
    getTurtle: (id) ->
      @_turtlesById[id] ? Nobody

    # (String, Number) => Agent
    getTurtleOfBreed: (breedName, id) ->
      turtle = @getTurtle(id)
      if turtle.getBreedName().toUpperCase() is breedName.toUpperCase()
        turtle
      else
        Nobody

    # (Object) => Unit
    importTurtles: (worldJSON) =>
      worldJSON["TURTLES"].forEach((turtle) =>
        newTurtle = @_createTurtle(turtle["who"], 0, 0, 0, 0, @_breedManager.turtles())
        for k,v of turtle when k != "who"
          if k == "breed"
            newTurtle.setVariable(k, @_breedManager.get(turtle["breed"].match(/{breed (.*)}/i)[1].toUpperCase()))
          else
            newTurtle.setVariable(k,v))
      @_idManager.setNextIndex(worldJSON["BUILT-IN GLOBALS"]["nextIndex"])
      return

    # () => TurtleSet
    turtles: ->
      new TurtleSet(@_turtles, "turtles")

    # (String) => TurtleSet
    turtlesOfBreed: (breedName) =>
      breed = @_breedManager.get(breedName)
      new TurtleSet(breed.members, breedName)

    # () => Unit
    _clearTurtlesSuspended: ->
      @_idManager.suspendDuring(() => @clearTurtles())
      return

    # (Number, Number, Number, Number, Number, Breed, String, Number, Boolean, Number, String, (Updatable) => PenManager) => Turtle
    _createTurtle: (id, color, heading, xcor, ycor, breed, label, lcolor, isHidden, size, shape, genPenManager) =>
      turtle = new Turtle(@_world, id, @_updater.updated, @_updater.registerPenTrail, @_updater.registerTurtleStamp, @_updater.registerDeadTurtle, @_createTurtle, @_removeTurtle, color, heading, xcor, ycor, breed, label, lcolor, isHidden, size, shape, genPenManager)
      @_updater.updated(turtle)(Builtins.turtleBuiltins...)
      @_turtles.push(turtle)
      @_turtlesById[id] = turtle
      turtle

    # (Number) => Unit
    _removeTurtle: (id) =>
      turtle = @_turtlesById[id]
      @_turtles.splice(@_turtles.indexOf(turtle), 1)
      delete @_turtlesById[id]
      return
