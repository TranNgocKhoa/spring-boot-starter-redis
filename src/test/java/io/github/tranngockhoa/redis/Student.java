package io.github.tranngockhoa.redis;

public class Student {
    private long id;
    private Person person;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static final class StudentBuilder {
        private long id;
        private Person person;

        private StudentBuilder() {
        }

        public StudentBuilder id(long id) {
            this.id = id;
            return this;
        }

        public StudentBuilder person(Person person) {
            this.person = person;
            return this;
        }

        public Student build() {
            Student student = new Student();
            student.setId(id);
            student.setPerson(person);
            return student;
        }
    }
}
