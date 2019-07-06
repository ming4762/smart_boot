ready(function () {
  require(['quartz/timedTask/TimedTask'], function ({TimedTask}) {
    var timedTask = new TimedTask()
    timedTask.init()
  })
})