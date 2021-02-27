package com.minerdev.faultlocalization.model

data class Person(
    val id: Int = 0,
    val name: String,
    val phone: String,
    val state: State,
    val type: Type
) {
    constructor(serializedData: SerializedData) {
        id = serializedData.uid
        name = serializedData.name
        phone = serializedData.phone
        state = State.values()[serializedData.state]
        type = Type.values()[serializedData.type]
    }

    constructor(name: String, phone: String, state: State, type: Type) {
        this.name = name
        this.phone = phone
        this.state = state
        this.type = type
    }

    enum class State(val name: String) {
        ONLINE("上线"), OFFLINE("下线");
    }

    enum class Type(val name: String) {
        MANAGER("管理"), REPAIRMAN("维修");
    }

    class SerializedData {
        var uid = 0
        var name = ""
        var phone = ""
        var state = 0
        var type = 0
    }
}