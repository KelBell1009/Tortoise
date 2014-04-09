// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.tortoise

case class Model(
    path: String,
    variation: String = "",
    dimensions: Option[(Int, Int, Int, Int)] = None,
    setup: String = "setup",
    go: String = "go",
    repetitions: Int,
    metrics: Seq[String] = Seq()) {
  def filename = new java.io.File(path).getName.stripSuffix(".nlogo")
  def name =
    if (variation.isEmpty)
      filename
    else
      s"$filename (${variation})"
}

object Model {
  // in no particular order
  val models = Seq[Model](
    Model(
      path = "models/Sample Models/Biology/Fireflies.nlogo",
      dimensions = Some((-10, 10, -10, 10)),
      setup = "set number 150  setup",
      repetitions = 20
    ),
    Model(
      path = "models/Sample Models/Biology/Wolf Sheep Predation.nlogo",
      variation = "no grass",
      setup = "set grass? true  setup",
      repetitions = 10,
      metrics = Seq("count wolves", "count sheep", "grass")
    ),
    Model(
      path = "models/Sample Models/Biology/Wolf Sheep Predation.nlogo",
      variation = "with grass",
      setup = "set grass? true  setup",
      repetitions = 10,
      metrics = Seq("count wolves", "count sheep", "grass")
    ),
    Model(
      path = "models/test/tortoise/Termites.nlogo",
      repetitions = 20
    ),
    Model(
      path = "models/Sample Models/Chemistry & Physics/Heat/Boiling.nlogo",
      dimensions = Some((-15, 15, -15, 15)),
      repetitions = 5
    ),
    Model(
      path = "models/Sample Models/Chemistry & Physics/Waves/Rope.nlogo",
      dimensions = Some((0, 40, -20, 20)),
      repetitions = 20
    ),
    Model(
      path = "models/test/tortoise/Sandpile.nlogo",
      variation = "random",
      dimensions = Some((-15, 15, -15, 15)),
      setup = """setup-random  set drop-location "center"""",
      repetitions = 100,
      metrics = Seq("total", "sizes", "lifetimes")
    ),
    Model(
      path = "models/test/tortoise/Sandpile.nlogo",
      variation = "uniform",
      dimensions = Some((-15, 15, -15, 15)),
      setup = """setup-uniform 0  set drop-location "center"""",
      repetitions = 100,
      metrics = Seq("total", "sizes", "lifetimes")
    ),
    Model(
      path = "models/Sample Models/Chemistry & Physics/Diffusion Limited Aggregation/DLA Simple.nlogo",
      dimensions = Some((-20, 20, -20, 20)),
      setup = "setup   set num-particles 100",
      repetitions = 25
    ),
    Model(
      path = "models/Sample Models/Earth Science/Fire.nlogo",
      dimensions = Some((-19, 19, -19, 19)),
      repetitions = 20
    ),
    Model(
      path = "models/test/tortoise/Life Simple.nlogo",
      repetitions = 30
    ),
    Model(
      path = "models/test/tortoise/Life Turtle-Based.nlogo",
      dimensions = Some((-10, 10, -10, 10)),
      setup = "setup-random",
      repetitions = 10
    ),
    Model(
      path = "models/Sample Models/Earth Science/Climate Change.nlogo",
      setup =
        "setup add-cloud add-cloud " +
        "repeat 10 [ add-CO2 ] " +
        "remove-cloud add-cloud " +
        "repeat 10 [ add-CO2 go remove-CO2 ] " +
        "remove-cloud",
      // unfortunately this takes quite a while on Nashorn, but it isn't a good test unless we run the
      // model for long enough for the turtles to start interacting with each other and with the world
      // boundaries - ST 10/10/13
      repetitions = 4,
      go = "repeat 50 [ go ]",
      metrics = Seq("temperature")
    ),
    Model(
      path = "models/Sample Models/Biology/Membrane Formation.nlogo",
      dimensions = Some((-10, 10, -10, 10)),
      setup = "set num-lipids 40 set num-water 100  setup",
      repetitions = 10
    ),
    Model(
      path = "models/Sample Models/Biology/Slime.nlogo",
      dimensions = Some((-10, 10, -10, 10)),
      setup = "set population 30  setup",
      repetitions = 10
    ),
    Model(
      path = "models/Sample Models/Social Science/Voting.nlogo",
      dimensions = Some((-10, 10, -10, 10)),
      repetitions = 10
    ),
    Model(
      path = "models/Sample Models/Networks/Preferential Attachment.nlogo",
      dimensions = Some((-10, 10, -10, 10)),
      repetitions = 20,
      go = "go  resize-nodes",
      metrics = Seq("[size] of turtles")
    ),
    Model(
      path = "models/Sample Models/Computer Science/Vants.nlogo",
      dimensions = Some((-25, 25, -25, 25)),
      repetitions = 1,
      go =
        "repeat 10 [ go-forward ] " +
        "repeat 10 [ go-reverse ]"
    ),
    Model(
      path = "models/Sample Models/Biology/Virus.nlogo",
      repetitions = 40
    ),
    Model(
      path = "models/Sample Models/Art/Follower.nlogo",
      dimensions = Some((-15, 15, -15, 15)),
      setup = "set population 500  setup",
      repetitions = 25
    ),
    Model(
      path = "models/Code Examples/Link Lattice Example.nlogo",
      dimensions = Some((-6, 6, -6, 6)),
      setup = "setup-square",
      repetitions = 1,
      go = "setup-hex"
    ),
    Model(
      path = "models/test/benchmarks/Ants Benchmark.nlogo",
      dimensions = Some((-20, 20, -20, 20)),
      repetitions = 3,
      go =
        "repeat 10 [ go ] " +
        "ask turtle 0 [ move-to one-of patches with [shade-of? pcolor blue] ]"
    ),
    Model(
      path = "models/test/benchmarks/Bureaucrats Benchmark.nlogo",
      dimensions = Some((0, 29, 0, 29)),
      repetitions = 200
    ),
    Model(
      path = "models/test/benchmarks/BZ Benchmark.nlogo",
      dimensions = Some((-20, 20, -20, 20)),
      repetitions = 5
    ),
    Model(
      path = "models/test/benchmarks/Heatbugs Benchmark.nlogo",
      dimensions = Some((0, 19, 0, 19)),
      repetitions = 10
    ),
    Model(
      path = "models/test/benchmarks/GasLabCirc Benchmark.nlogo",
      dimensions = Some((-20, 20, -20, 20)),
      setup = "set number 50  setup",
      repetitions = 50
    ),
    Model(
      path = "models/test/benchmarks/CA1D Benchmark.nlogo",
      dimensions = Some((-9, 9, -4, 4)),
      setup = "setup-random",
      repetitions = 20
    ),
    Model(
      path = "models/Code Examples/State Machine Example.nlogo",
      dimensions = Some((-19, 19, -19, 19)),
      setup = "set number 100  setup",
      repetitions = 100
    )
  )
}