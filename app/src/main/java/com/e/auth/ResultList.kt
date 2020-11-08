package com.e.auth

class ResultList(var resList: List<FileData>) {
    fun getResults(): List<FileData>{
        return resList
    }
}