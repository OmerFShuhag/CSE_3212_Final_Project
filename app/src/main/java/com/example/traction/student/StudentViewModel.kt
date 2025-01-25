import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traction.student.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class StudentViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun userCollectionPath(userId: String): String = "user/$userId/students"

    // LiveData for observing student list
    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students

    // LiveData for observing a single student
    private val _selectedStudent = MutableLiveData<Student?>()
    val selectedStudent: LiveData<Student?> get() = _selectedStudent

    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val userId = auth.currentUser?.uid

    // **1. Add Student**
    fun addStudent(student: Student) {
        val studentCollection = firestore.collection(userCollectionPath(userId.toString()))
        studentCollection.document(student.id)
            .set(student)
            .addOnSuccessListener {
                if (userId != null) {
                    fetchStudents()
                }
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Failed to add student: ${exception.message}"
            }
    }

    // **2. Fetch All Students**
    fun fetchStudents() {
        val studentCollection = firestore.collection(userCollectionPath(userId.toString()))
        studentCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val students = querySnapshot.documents.mapNotNull { it.toObject<Student>() }
                _students.value = students
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Failed to fetch students: ${exception.message}"
            }
    }

    // **3. Fetch Single Student**
    fun fetchStudentById(studentId: String) {
        val studentDocument = firestore.collection(userCollectionPath(userId.toString())).document(studentId)
        studentDocument.get()
            .addOnSuccessListener { documentSnapshot ->
                _selectedStudent.value = documentSnapshot.toObject()
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Failed to fetch student: ${exception.message}"
            }
    }

    // **4. Update Student**
    fun updateStudent(student: Student) {
        val studentDocument = firestore.collection(userCollectionPath(userId.toString())).document(student.id)
        studentDocument.set(student)
            .addOnSuccessListener {
                // You could trigger a reload of students here
                fetchStudents()
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Failed to update student: ${exception.message}"
            }
    }

    // **5. Delete Student**
    fun deleteStudent(userId: String, studentId: String) {
        val studentDocument = firestore.collection(userCollectionPath(userId)).document(studentId)
        studentDocument.delete()
            .addOnSuccessListener {
                // You could trigger a reload of students here
                fetchStudents()
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Failed to delete student: ${exception.message}"
            }
    }

    // **6. Clear Error Message**
    fun clearError() {
        _errorMessage.value = null
    }
}
