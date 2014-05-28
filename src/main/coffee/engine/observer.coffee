define(['engine/patch', 'engine/turtle', 'engine/variablemanager', 'integration/lodash']
     , ( Patch,          Turtle,          VariableManager,          _) ->

  class Observer

    id: 0

    _varManager: undefined # VariableManager

    _perspective: undefined # Number
    _targetAgent: undefined # (Number, Number)

    _codeGlobalNames: undefined # Array[String]

    # (Updater, Array[String], Array[String]) => Observer
    constructor: (@_updater, @_globalNames, @_interfaceGlobalNames) ->
      @resetPerspective()
      @_varManager      = new VariableManager(@_globalNames)
      @_codeGlobalNames = _(@_globalNames).difference(@_interfaceGlobalNames)

    # (Agent) => Unit
    watch: (agent) ->
      @_perspective = 3
      @_targetAgent =
        if agent instanceof Turtle
          [1, agent.id]
        else if agent instanceof Patch
          [2, agent.id]
        else
          [0, -1]
      @_updatePerspective()
      return

    # () => Unit
    resetPerspective: ->
      @_perspective = 0
      @_targetAgent = null
      @_updatePerspective()
      return

    # (String) => Any
    getGlobal: (varName) ->
      @_varManager.get(varName)

    # (String, Any) => Unit
    setGlobal: (varName, value) ->
      @_varManager.set(varName, value)

    # () => Unit
    clearCodeGlobals: ->
      _(@_codeGlobalNames).forEach((name) => @_varManager.set(name, 0); return)
      return

    # () => Unit
    _updatePerspective: ->
      @_updater.updated(this)("perspective", "targetAgent")
      return

)
