// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.tortoise

import
  org.nlogo.{ api, core, shape },
    api.{ AgentVariables, Program, ShapeList, Version },
    core.{ AgentKind, Model },
    shape.{ LinkShape, VectorShape }

import
  org.nlogo.tortoise.json.JSONSerializer

// RuntimeInit generates JavaScript code that does any initialization that needs to happen
// before any user code runs, for example creating patches

class RuntimeInit(program: Program, model: Model) {

  def init: String =
    s"""var workspace     = require('engine/workspace')($genBreedObjects)($genWorkspaceArgs);
       |var AgentSet      = workspace.agentSet;
       |var BreedManager  = workspace.breedManager;
       |var LayoutManager = workspace.layoutManager;
       |var LinkPrims     = workspace.linkPrims;
       |var Prims         = workspace.prims;
       |var Updater       = workspace.updater;
       |var world         = workspace.world;
       |
       |var Call       = require('engine/call');
       |var ColorModel = require('engine/colormodel');
       |var Dump       = require('engine/dump');
       |var Exception  = require('engine/exception');
       |var Link       = require('engine/link');
       |var LinkSet    = require('engine/linkset');
       |var Nobody     = require('engine/nobody');
       |var PatchSet   = require('engine/patchset');
       |var Tasks      = require('engine/tasks');
       |var Trig       = require('engine/trig');
       |var Turtle     = require('engine/turtle');
       |var TurtleSet  = require('engine/turtleset');
       |var Type       = require('engine/typechecker');
       |
       |var AgentModel     = require('integration/agentmodel');
       |var Denuller       = require('integration/denuller');
       |var notImplemented = require('integration/notimplemented');
       |var Random         = require('integration/random');
       |var StrictMath     = require('integration/strictmath');""".stripMargin

  private def genBreedObjects: String = {
    val breedObjs =
      program.breeds.values.map {
        b =>
          val name     = s"'${b.name}'"
          val singular = s"'${b.singular.toLowerCase}'"
          val varNames = mkJSArrStr(b.owns map (_.toLowerCase) map wrapInQuotes)
          s"""{ name: $name, singular: $singular, varNames: $varNames }"""
      }
    mkJSArrStr(breedObjs)
  }

  private def genWorkspaceArgs: String = {

    import scala.collection.JavaConverters.asScalaBufferConverter

    def shapeList(shapes: ShapeList): String = {
      import scala.collection.JavaConverters.asScalaSetConverter
      if (shapes.getNames.asScala.nonEmpty)
        JSONSerializer.serialize(shapes)
      else
        "{}"
    }

    def parseTurtleShapes(strings: Array[String]): ShapeList =
      new ShapeList(AgentKind.Turtle, VectorShape.parseShapes(strings, Version.version).asScala)

    def parseLinkShapes(strings: Array[String]): ShapeList =
      new ShapeList(AgentKind.Link, LinkShape.parseShapes(strings, Version.version).asScala)

    // The turtle varnames information is only used by the `Turtle` class, which already has intrinsic knowledge of many
    // variables (often in special-cased form, e.g. `color`), so we should only bother passing in turtles-own variables
    // that aren't intrinsic to the class. --JAB (5/29/14)
    val linkVarNames   = program.linksOwn   diff AgentVariables.getImplicitLinkVariables
    val patchVarNames  = program.patchesOwn diff AgentVariables.getImplicitPatchVariables
    val turtleVarNames = program.turtlesOwn diff AgentVariables.getImplicitTurtleVariables

    val globalNames          = mkJSArrStr(program.globals          map (_.toLowerCase) map wrapInQuotes)
    val interfaceGlobalNames = mkJSArrStr(program.interfaceGlobals map (_.toLowerCase) map wrapInQuotes)
    val linksOwnNames        = mkJSArrStr(linkVarNames             map (_.toLowerCase) map wrapInQuotes)
    val patchesOwnNames      = mkJSArrStr(patchVarNames            map (_.toLowerCase) map wrapInQuotes)
    val turtlesOwnNames      = mkJSArrStr(turtleVarNames           map (_.toLowerCase) map wrapInQuotes)

    val turtleShapesJson = shapeList(parseTurtleShapes(model.turtleShapes.toArray))
    val linkShapesJson   = shapeList(parseLinkShapes(model.linkShapes.toArray))

    val view = model.view
    import view._

    s"$globalNames, $interfaceGlobalNames, $turtlesOwnNames, $linksOwnNames, $patchesOwnNames, $minPxcor, $maxPxcor, " +
      s"$minPycor, $maxPycor, $patchSize, $wrappingAllowedInX, $wrappingAllowedInY, $turtleShapesJson, $linkShapesJson"

  }

  private def wrapInQuotes(str: String): String =
    s"'$str'"

  private def mkJSArrStr(arrayValues: Iterable[String]): String =
    if (arrayValues.nonEmpty)
      arrayValues.mkString("[", ", ", "]")
    else
      "[]"

}
