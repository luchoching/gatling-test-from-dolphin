Official demo stress tests: 

```
$ sbt "testOnly computerdatabase.BasicSimulation"

$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep01
$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep02
$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep03
$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep04
$ sbt "testOnly computerdatabase.advanced.AdvancedSimulationStep05

```


#  Street Test (StartGate + Mercury): 

User `sbt run` to generate access tokens file(`tokens.csv`). 

Access tokens are made from testfriend100-999. 

Then run test: 

```
$ sbt "testOnly fromdolphin.BasicSimulation"
```

