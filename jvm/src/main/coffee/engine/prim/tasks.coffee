# (C) Uri Wilensky. https://github.com/NetLogo/Tortoise

{ all, length, map } = require('brazierjs/array')
{ pipeline }         = require('brazierjs/function')
{ rangeUntil }       = require('brazierjs/number')

Exception = require('util/exception')

module.exports = {

  # (Function) => Function
  commandTask: (fn) ->
    fn.isReporter = false
    fn

  # (Function) => Function
  reporterTask: (fn) ->
    fn.isReporter = true
    fn

  # [Result] @ (Product => Result, Array[Any]) => Result
  apply: (fn, args) ->
    if args.length >= fn.length
      fn.apply(fn, args)
    else
      pluralStr = if fn.length is 1 then "" else "s"
      throw new Error("anonymous procedure expected #{fn.length} input#{pluralStr}, but only got #{args.length}")

  # [Result] @ (Product => Result, Array[Any]*) => Array[Result]
  map: (fn, lists...) ->
    @_processLists(fn, lists, "map")

  # [Result] @ (Number, (Number) => Result) => Array[Result]
  nValues: (n, fn) ->
    map(fn)(rangeUntil(0)(n))

  # [Result] @ (Product => Result, Array[Any]*) => Unit
  forEach: (fn, lists...) ->
    @_processLists(fn, lists, "foreach")
    return

  # [Result] @ (Product => Result, Array[Array[Any]], String) => Array[Result]
  _processLists: (fn, lists, primName) ->
    numLists = lists.length
    head     = lists[0]
    if numLists is 1
      map(fn)(head)
    else if all((l) -> l.length is head.length)(lists)
      for i in [0...head.length]
        fn(map((list) -> list[i])(lists)...)
    else
      throw new Error("All the list arguments to #{primName.toUpperCase()} must be the same length.")

}
