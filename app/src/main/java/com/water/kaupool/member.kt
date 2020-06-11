package com.water.kaupool

class member {
    var member_id: String? = null
    var member_pw: String? = null
    var member_name: String? = null
    var member_phone: String? = null
    var member_gender: String? = null
    var member_age: String? = null
    var option1 = false
    var option2 = false
    var option3 = false
    var option4 = false

    constructor(member_id: String?, member_pw: String?, member_name: String?,
                member_phone: String?, member_age: String?, member_gender: String?,
                option1: Boolean, option2: Boolean, option3: Boolean, option4: Boolean) {
        this.member_id = member_id
        this.member_pw = member_pw
        this.member_name = member_name
        this.member_phone = member_phone
        this.member_gender = member_gender
        this.member_age = member_age
        this.option1 = true
        this.option2 = true
        this.option3 = true
        this.option4 = true
    }

    constructor() {}

}