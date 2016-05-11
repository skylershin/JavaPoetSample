import com.squareup.javapoet.*;
import com.sun.xml.internal.bind.api.TypeReference;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;

public class JavaPoetMain {
    private static String PACKAGE_NAME = "com.example.javapoet";

    public static void main(String args[]) {
        System.out.print("JavaPoet Test Sample\n");

        // University Class
        TypeSpec universityTypeSpec = makeUniversityClass();

        // user
        FieldSpec name = FieldSpec.builder(String.class, "name")
                .addModifiers(Modifier.PRIVATE)
                .build();

        // age
        FieldSpec age = FieldSpec.builder(Integer.class, "age")
                .addModifiers(Modifier.PRIVATE)
                .build();

        // university
        ClassName universityClassName = ClassName.get(PACKAGE_NAME, "User", "University");
        FieldSpec university = FieldSpec.builder(universityClassName, "university")
                .addModifiers(Modifier.PRIVATE)
                .build();

        // friends
        ClassName userClassName = ClassName.get(PACKAGE_NAME, "User");
        ClassName listClassName = ClassName.get("java.util", "List");
        TypeName listOfUserClassName = ParameterizedTypeName.get(listClassName, userClassName);
        FieldSpec friends = FieldSpec.builder(listOfUserClassName, "friends")
                .addModifiers(Modifier.PRIVATE)
                .build();

        // User class
        TypeSpec user = TypeSpec.classBuilder("User")
                .addModifiers(Modifier.PUBLIC)
                .addField(name)
                .addField(age)
                .addField(university)
                .addField(friends)
                .addMethod(makeGetter(String.class,"name"))
                .addMethod(makeSetter(String.class,"name"))
                .addMethod(makeGetter(Integer.class,"age"))
                .addMethod(makeSetter(Integer.class,"age"))
                .addMethod(makeGetter(listOfUserClassName, "friends"))
                .addMethod(makeSetter(listOfUserClassName, "friends"))
                .addMethod(makeGetter(universityClassName, "university"))
                .addMethod(makeSetter(universityClassName, "university"))
                .addType(universityTypeSpec)
                .build();

        // JavaFile
        JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, user)
                .build();

        // print log
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static TypeSpec makeUniversityClass() {
        FieldSpec university = FieldSpec.builder(String.class, "university")
                .addModifiers(Modifier.PRIVATE)
                .build();
        FieldSpec major = FieldSpec.builder(String.class, "major")
                .addModifiers(Modifier.PRIVATE)
                .build();

        return TypeSpec.classBuilder("University")
                .addField(university)
                .addField(major)
                .addMethod(makeGetter(String.class, "university"))
                .addMethod(makeGetter(String.class, "major"))
                .addMethod(makeSetter(String.class, "university"))
                .addMethod(makeSetter(String.class, "major"))
                .build();
    }

    /**
     * Utility Functions
     */
    private static MethodSpec makeSetter(Type type, String fieldName) {
        String methodName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return MethodSpec.methodBuilder("set" + methodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, fieldName)
                .returns(void.class)
                .addStatement("this.$L = $L", fieldName, fieldName)
                .build();
    }

    private static MethodSpec makeGetter(Type type, String fieldName) {
        String methodName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return MethodSpec.methodBuilder("get" + methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(type)
                .addStatement("return $L", fieldName)
                .build();
    }

    private static MethodSpec makeSetter(TypeName typeName, String fieldName) {
        String methodName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return MethodSpec.methodBuilder("set" + methodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(typeName, fieldName)
                .returns(void.class)
                .addStatement("this.$L = $L", fieldName, fieldName)
                .build();
    }


    private static MethodSpec makeGetter(TypeName typeName, String fieldName) {
        String methodName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return MethodSpec.methodBuilder("get" + methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(typeName)
                .addStatement("return $L", fieldName)
                .build();
    }
}
