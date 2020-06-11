package com.water.kaupool

class manager {
    //var date: String?=null
    var date: String = ""
    var time: String? = null
    var start: String? = null
    var end: String? = null
    var master: String? = null
    var phone: String? = null
    var user: String? = null
    var num = 0
    var choice = false
    var check = false


    internal constructor() {}
    internal constructor(date: String, time: String?, master: String?, phone: String?, user: String?,
                         choice: Boolean, start: String?, end: String?, num: Int) {
        this.date = date
        this.time = time
        this.start = start
        this.end = end
        this.master = master
        this.phone = phone
        this.user = user
        this.num = num
        this.choice = choice
        this.check = true
    }

}