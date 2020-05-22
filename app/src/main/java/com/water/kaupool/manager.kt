package com.water.kaupool

class manager {
    // get set method
    //var date: String?=null
    var date: String = ""
    var time: String? = null
    var start: String? = null
    var end: String? = null
    var master: String? = null
    var user: String? = null
    var num = 0
    var choice = false
    var check = false


    internal constructor() {}
    internal constructor(date: String, time: String?, master: String?, user: String?, choice: Boolean,
                         start: String?, end: String?, num: Int) {
        this.date = date
        this.time = time
        this.start = start
        this.end = end
        this.master = master
        this.user = user
        this.num = num
        this.choice = choice
        this.check = true
    }

}