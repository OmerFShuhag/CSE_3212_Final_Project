package com.example.traction

class Validator {

    companion object{
        private val email_regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        //private val password_regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,20}$")
        private val password_regex = Regex("^.{8,}$")
        private val name_regex = Regex("^[A-Za-z]{2,}(\\s[A-Za-z]{2,})?\$")
        private val phone_regex = Regex("^01[346789]\\d{8}\$")
        private val salary_regex = Regex("^[0-9]+\$")

        fun validateEmail(email: String): String?{
            return when{
                email.isEmpty() -> "Emails cannot be Empty"
                !email_regex.matches(email) -> "Invalid Email Address"
                else -> null
            }
        }

        fun validateName(name: String): String? {
            return when {
                name.isEmpty() -> "Name cannot be empty"
                !name_regex.matches(name) -> "Name must contain only letters and be at least 2 characters"
                else -> null
            }
        }

        fun validatePassword(password: String): String? {
            return when {
                password.isEmpty() -> "Password cannot be empty"
                !password_regex.matches(password) -> "Password must be 6-20 characters, include 1 uppercase, 1 lowercase, and 1 digit"
                else -> null
            }
        }

        fun validateConfirmPassword(password: String, confirmPassword: String): String? {
            return when {
                confirmPassword.isEmpty() -> "Confirm Password cannot be empty"
                password != confirmPassword -> "Passwords do not match"
                else -> null
            }
        }

        fun validatePhone(phone: String): String? {
            return when {
                phone.isEmpty() -> "Phone number cannot be empty"
                !phone_regex.matches(phone) -> "Invalid Phone number"
                else -> null
            }
        }

        fun validateAddress(address: String): String? {
            return when {
                address.isEmpty() -> "Address cannot be empty"
                else -> null
            }
        }

        fun validateSalary(salary: String): String? {
            return when {
                salary.isEmpty() -> "Salary cannot be empty"
                !salary_regex.matches(salary) -> "Invalid Salary"
                else -> null
            }
        }

    }
}

