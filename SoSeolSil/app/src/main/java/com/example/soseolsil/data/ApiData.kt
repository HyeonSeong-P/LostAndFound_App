package com.example.soseolsil.data


class ApiData {
    var actID: String? = null
    var lstPlace: String? = null
    var lstPrdtNm: String? = null
    var lstSbjt: String? = null
    var lstYmd: String? = null
    var prdtClNm: String? = null
    var rnum: String? = null

    constructor() {}
    constructor(
        actID: String?,
        lstPlace: String?,
        lstPrdNm: String?,
        lstSbjt: String?,
        lstYmd: String?,
        prdtClNm: String?,
        rNum: String?
    ) {
        this.actID = actID
        this.lstPlace = lstPlace
        lstPrdtNm = lstPrdNm
        this.lstSbjt = lstSbjt
        this.lstYmd = lstYmd
        this.prdtClNm = prdtClNm
        rnum = rnum
    }

    fun toMap(): Map<String, Any?> {
        val result: HashMap<String, Any?> = HashMap()
        result["actID"] = actID
        result["lstPlace"] = lstPlace
        result["lstPrdtNm"] = lstPrdtNm
        result["lstSbjt"] = lstSbjt
        result["lstYmd"] = lstYmd
        result["prdtClNm"] = prdtClNm
        result["rnum"] = rnum
        return result
    }
}