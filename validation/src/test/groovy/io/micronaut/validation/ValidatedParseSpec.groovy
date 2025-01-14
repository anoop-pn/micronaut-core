package io.micronaut.validation

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec
import io.micronaut.aop.Around
import io.micronaut.inject.ProxyBeanDefinition
import io.micronaut.inject.writer.BeanDefinitionVisitor
import io.micronaut.inject.writer.BeanDefinitionWriter
import spock.lang.PendingFeature

import java.time.LocalDate

class ValidatedParseSpec extends AbstractTypeElementSpec {
    void "test constraints on beans make them @Validated"() {
        given:
        def definition = buildBeanDefinition('test.$Test' + BeanDefinitionWriter.CLASS_SUFFIX + BeanDefinitionVisitor.PROXY_SUFFIX,'''
package test;

@jakarta.inject.Singleton
class Test {

    @io.micronaut.context.annotation.Executable
    public void setName(@javax.validation.constraints.NotBlank String name) {

    }

    @io.micronaut.context.annotation.Executable
    public void setName2(@javax.validation.Valid String name) {

    }
}
''')

        expect:
        definition instanceof ProxyBeanDefinition
        definition.findMethod("setName", String).get().hasStereotype(Validated)
        definition.findMethod("setName2", String).get().getAnnotationTypesByStereotype(Around).contains(Validated)
    }

    void "test constraints on a declarative client makes it @Validated"() {
        given:
        def definition = buildBeanDefinition('test.ExchangeRates' + BeanDefinitionVisitor.PROXY_SUFFIX,'''
package test;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Client("https://exchangeratesapi.io")
interface ExchangeRates {

    @Get("{date}")
    String rate(@PastOrPresent LocalDate date);
}
''')

        expect:
        definition.findMethod("rate", LocalDate).get().hasStereotype(Validated)
    }


    void "test a default method constraints on a declarative client makes it @Validated"() {
        given:
            def definition = buildBeanDefinition('validateparse3.ExchangeRates' + BeanDefinitionVisitor.PROXY_SUFFIX,'''
package validateparse3;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Client("https://exchangeratesapi.io")
interface ExchangeRates {

    @Get("{date}")
    String rate(@PastOrPresent LocalDate date);

    default String rate2(@PastOrPresent LocalDate date) {
        return null;
    }
}
''')

        expect:
            definition.findMethod("rate", LocalDate).get().hasStereotype(Validated)
            definition.findMethod("rate2", LocalDate).get().hasStereotype(Validated)
    }

    @PendingFeature
    void "test a default method only constraints on a declarative client makes it @Validated"() {
        given:
            def definition = buildBeanDefinition('validateparse3.ExchangeRates' + BeanDefinitionVisitor.PROXY_SUFFIX,'''
package validateparse3;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Client("https://exchangeratesapi.io")
interface ExchangeRates {

    @Get("{date}")
    String rate(LocalDate date);

    default String rate2(@PastOrPresent LocalDate date) {
        return rate(date);
    }
}
''')

        expect:
            !definition.findMethod("rate", LocalDate).get().hasStereotype(Validated)
            definition.findMethod("rate2", LocalDate).get().hasStereotype(Validated)
    }
}
