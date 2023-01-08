package kr.asyu.rpg.statlib;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({"kr.asyu.rpg.statlib.annotations.IntegerStatMember", "kr.asyu.rpg.statlib.annotations.FloatStatMember"})
public class StatCalcAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            Set<VariableElement> fields = ElementFilter.fieldsIn(elements);
            for (VariableElement field : fields) {
                TypeMirror fieldType = field.asType();
                String fullTypeClassName = fieldType.toString();

                Class<?> numberClass = getNumberClass(annotation);

                if (numberClass == null) {
                    processingEnv.getMessager().printMessage(
                        Kind.WARNING, "Not Found Number Class!", field);
                } else {
                    if (!numberClass.getName().equals(fullTypeClassName)) {
                        processingEnv.getMessager().printMessage(
                            Kind.ERROR, "Field type must be " + numberClass.getName() + ", but current type is " + fullTypeClassName, field);
                    }
                }
            }
        }
        return false;
    }

    private Class<?> getNumberClass(TypeElement annotation) {
        String annotationClass = annotation.asType().toString();
        if (annotationClass.equals("kr.asyu.rpg.statlib.annotations.IntegerStatMember")) {
            return Long.class;
        } else if (annotationClass.equals("kr.asyu.rpg.statlib.annotations.FloatStatMember")) {
            return Double.class;
        }
        return null;
    }
}