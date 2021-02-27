package com.minerdev.faultlocalization.model

class Message {
    var uid = 0
        private set
    var fromWho: String?
    var contents: String?
    var state: State
    var type: Type

    constructor(serializedData: SerializedData) {
        uid = serializedData.uid
        fromWho = serializedData.from_who
        contents = serializedData.contents
        state = State.values()[serializedData.state]
        type = Type.values()[serializedData.type]
    }

    constructor(from_who: String?, contents: String?, state: State, type: Type) {
        fromWho = from_who
        this.contents = contents
        this.state = state
        this.type = type
    }

    enum class State(override val name: String) {
        UNDONE("未完成"), DOING("进行中"), DONE("已完成");

    }

    enum class Type(override val name: String) {
        REGISTER_REQUEST("注册"), REPAIR_REQUEST("维修申请"), REPAIR_DONE("维修完成");

    }

    inner class SerializedData {
        var uid = 0
        var from_who: String? = null
        var contents: String? = null
        var state = 0
        var type = 0
    }
}