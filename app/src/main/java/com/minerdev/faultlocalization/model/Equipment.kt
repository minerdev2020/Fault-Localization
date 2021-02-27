package com.minerdev.faultlocalization.model

class Equipment {
    var uid = 0
        private set
    var name: String?
    var number: String?
    var state: State
    var type: String?

    constructor(serializedData: SerializedData) {
        uid = serializedData.uid
        name = serializedData.name
        number = serializedData.number
        state = State.values()[serializedData.state]
        type = serializedData.type
    }

    constructor(name: String?, number: String?, state: State, type: String?) {
        this.name = name
        this.number = number
        this.state = state
        this.type = type
    }

    enum class State(override val name: String) {
        NORMAL("正常"), REPAIR("维修中"), STOP("停用");

    }

    inner class SerializedData {
        var uid = 0
        var name: String? = null
        var number: String? = null
        var state = 0
        var type: String? = null
    }
}