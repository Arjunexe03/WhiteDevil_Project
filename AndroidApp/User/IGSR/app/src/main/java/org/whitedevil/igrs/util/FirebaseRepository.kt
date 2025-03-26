package org.whitedevil.igrs.util

import com.google.firebase.Firebase
import com.google.firebase.database.database
import org.whitedevil.igrs.ui.items.Problem

class FirebaseRepository {
    private val database = Firebase.database
    private val reference = database.getReference("problems")

    fun addProblem(problem: Problem, onComplete: (Boolean) -> Unit) {
        val problemId = reference.push().key
        problemId?.let {
            reference.child(it).setValue(problem)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } ?: onComplete(false)
    }
}
