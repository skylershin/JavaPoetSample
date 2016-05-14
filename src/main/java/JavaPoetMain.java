import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MunkyuShin on 5/11/16.
 */

public class JavaPoetMain {
    public static void main(String[] args) {
        JavaFile javaFile = JavaFile.builder("com.example", generateUserType())
                .build();

        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static TypeSpec generateUserType() {
        String classNameText = "User";
        String innerClassNameText = "University";
        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field("name", String.class));
        fields.add(new Field("age", Integer.class));
        TypeSpec.Builder builder = generatePOJOClass(classNameText, fields);

        List<Field> innerClassFields = new ArrayList<Field>();
        innerClassFields.add(new Field("university", String.class));
        innerClassFields.add(new Field("major", String.class));

        builder.addType(generatePOJOClass(innerClassNameText, innerClassFields).build());
        builder = generateHavingInnerClassVariableClass(builder, innerClassNameText);
        builder = generateHavingListVariableClass(builder, "User", "friends");

        return builder.build();
    }

    private static TypeSpec.Builder generateHavingListVariableClass(TypeSpec.Builder builder, String classNameText, String listFieldName) {
        ClassName className = ClassName.get("com.example", classNameText);

        TypeName listFieldType = ParameterizedTypeName.get(ClassName.get(List.class), className);
        FieldSpec fieldSpec = generateField(listFieldType, listFieldName);

        builder.addField(fieldSpec);
        builder.addMethod(generateGetMethod(listFieldName));
        builder.addMethod(generateSetMethod(listFieldName));
        return builder;
    }
    private static TypeSpec.Builder generateHavingInnerClassVariableClass(TypeSpec.Builder builder, String innerClassNameText) {
        ClassName innerClassName = ClassName.get("com.example", innerClassNameText);
        FieldSpec innerClassField = generateField(innerClassName, innerClassNameText);

        builder.addField(innerClassField);
        builder.addMethod(generateGetMethod(innerClassNameText));
        builder.addMethod(generateSetMethod(innerClassNameText));

        return builder;
    }

    private static TypeSpec.Builder generatePOJOClass(String classNameText, List<Field> fields) {
        ClassName className = ClassName.get("com.example", classNameText);

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addFields(generateFields(fields))
                .addMethods(generateConstructors(fields))
                .addMethods(generateGetSetMethods(fields));
    }

    private static List<MethodSpec> generateConstructors(List<Field> fields) {
        List<MethodSpec> constructors = new ArrayList<MethodSpec>();

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        constructors.add(constructorBuilder.build());

        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            constructorBuilder
                    .addParameter(field.getFieldType(), field.getFieldName())
                    .addStatement("this.$N = $N", field.getFieldName(), field.getFieldName());
            constructors.add(constructorBuilder.build());
        }
        return constructors;
    }

    private static List<FieldSpec> generateFields(List<Field> fields) {
        List<FieldSpec> fieldSpecs = new ArrayList<FieldSpec>();
        for (Field field : fields) {
            fieldSpecs.add(generateField(field));
        }
        return fieldSpecs;
    }

    private static FieldSpec generateField(Field field) {
        return FieldSpec.builder(field.getFieldType(), field.getFieldName())
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    private static FieldSpec generateField(TypeName typeName, String fieldName) {
        return FieldSpec.builder(typeName, fieldName)
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    private static List<MethodSpec> generateGetSetMethods(List<Field> fields) {
        List<MethodSpec> getSets = new ArrayList<MethodSpec>();

        for (Field field : fields) {
            getSets.add(generateSetMethod(field.getFieldName()));
            getSets.add(generateGetMethod(field.getFieldName()));
        }

        return getSets;
    }

    private static MethodSpec generateSetMethod(String fieldName) {
        String methodName = "set" + fieldName;
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(String.class, fieldName.toLowerCase())
                .addStatement("this.$N = $N", fieldName.toLowerCase(), fieldName.toLowerCase())
                .build();
    }

    private static MethodSpec generateGetMethod(String fieldName) {
        String methoidName = "get" + fieldName;
        return MethodSpec.methodBuilder(methoidName)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $N", fieldName)
                .build();

    }

    private static class Field {
        private String fieldName;
        private Type fieldType;

        public Field(String fieldName, Type fieldType) {
            this.fieldName = fieldName;
            this.fieldType = fieldType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Type getFieldType() {
            return fieldType;
        }

        public void setFieldType(Type fieldType) {
            this.fieldType = fieldType;
        }
    }
}
