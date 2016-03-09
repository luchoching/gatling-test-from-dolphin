Official demo stress tests: 

```
$ sbt "testOnly computerdatabase.BasicSimulation"

$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep01
$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep02
$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep03
$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep04
$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep05

```


Street Test (Concentor + ACE): 

First change the accessToken value at `ReqMsg.scala`, then: 


```
$ sbt "testOnly fromdolphin.BasicSimulation"
```

