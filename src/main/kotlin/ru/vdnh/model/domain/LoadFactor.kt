package ru.vdnh.model.domain

data class LoadFactor(
    val morning: Int,
    val day: Int,
    val evening: Int,
    val night: Int,
) {

    fun getFactor(): Int {
        // TODO
        return this.day
    }

}
