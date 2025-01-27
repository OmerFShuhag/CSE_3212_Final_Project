import android.content.Context
import android.util.Log
import android.widget.Toast
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


    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students


    private val _selectedStudent = MutableLiveData<Student?>()
    val selectedStudent: LiveData<Student?> get() = _selectedStudent


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val userId = auth.currentUser?.uid


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


    fun updateStudent(student: Student, studentId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("user").document(userId.toString())
            .collection("students").document(studentId)
            .set(student)
            .addOnSuccessListener {
                Log.d("Update", "Student updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Update", "Error updating student: ${e.message}", e)
            }
    }

    fun deleteStudent(studentId: String) {
        val studentDocument = firestore.collection(userCollectionPath(userId.toString())).document(studentId)
        studentDocument.delete()
            .addOnSuccessListener {
                fetchStudents()
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Failed to delete student: ${exception.message}"
            }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun markAttendance(studentId: String, context: Context) {
        val attendanceDate = getCurrentDate()

        val studentRef = firestore.collection(userCollectionPath(userId.toString()))
            .document(studentId)

        studentRef.get().addOnSuccessListener { documentSnapshot ->
            val student = documentSnapshot.toObject<Student>()
            if (student != null) {
                if (!student.attendance.contains(attendanceDate)) {
                    val updatedAttendance = student.attendance + attendanceDate
                    studentRef.update("attendance", updatedAttendance)
                        .addOnSuccessListener {
                            val message = "Attendance marked for ${student.name} on $attendanceDate"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            fetchAttendance(studentId)
                        }
                        .addOnFailureListener { exception ->
                            _errorMessage.value = "Failed to mark attendance: ${exception.message}"
                        }
                }
                else{
                    val message = "Attendance already marked for ${student.name} on $attendanceDate"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return format.format(java.util.Date())  // Return the current date
    }

    fun fetchAttendance(studentId: String) {
        val studentRef = firestore.collection(userCollectionPath(userId.toString()))
            .document(studentId)

        studentRef.get().addOnSuccessListener { documentSnapshot ->
            val student = documentSnapshot.toObject<Student>()
            if (student != null) {
                _selectedStudent.value = student

            }
        }.addOnFailureListener { exception ->
            _errorMessage.value = "Failed to fetch attendance: ${exception.message}"
        }
    }


}
