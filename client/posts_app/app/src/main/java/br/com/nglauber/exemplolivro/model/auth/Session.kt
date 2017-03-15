package br.com.nglauber.exemplolivro.model.auth

interface Session {
    fun getCurrentUser() : User?
}