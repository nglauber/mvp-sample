package br.com.nglauber.exemplolivro.model.auth

interface OnAuthRequestedListener {
    fun onAuthSuccess()
    fun onAuthCancel()
    fun onAuthError()
}
