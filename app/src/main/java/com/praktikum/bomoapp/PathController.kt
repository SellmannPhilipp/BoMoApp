package com.praktikum.bomoapp

class PathController {
    companion object {
        private var pathToShow = 2

        fun setPathToShow(pathToShow: Int) {
            this.pathToShow = pathToShow
        }

        fun getPathToShow(): Int {
            return this.pathToShow
        }
    }
}