package com.water.kaupool

class member {
    var member_id: String? = null
    var member_pw: String? = null
    var member_name: String? = null
    var member_phone: String? = null
        private set
    private var member_gender: String? = null

    constructor(member_id: String?, member_pw: String?, member_name: String?, member_phone: String?, member_gender: String?) {
        this.member_id = member_id
        this.member_pw = member_pw
        this.member_name = member_name
        this.member_phone = member_phone
        this.member_gender = member_gender
    }

    constructor() {}

}