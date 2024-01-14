package org.example.analyzer;

import de.fraunhofer.aisec.cpg.ConfigurationException;
import de.fraunhofer.aisec.cpg.TranslationConfiguration;
import de.fraunhofer.aisec.cpg.TranslationManager;
import de.fraunhofer.aisec.cpg.TranslationResult;
import de.fraunhofer.aisec.cpg.frontends.cxx.CLanguage;
import de.fraunhofer.aisec.cpg.frontends.cxx.CPPLanguage;
import de.fraunhofer.aisec.cpg.passes.ControlDependenceGraphPass;
import kotlin.reflect.KClass;
import kotlin.reflect.jvm.internal.ReflectionFactoryImpl;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TranslationLoader {
    public static TranslationResult build(List<File> files) throws ExecutionException, InterruptedException, ConfigurationException {
        ReflectionFactoryImpl reflectionFactory = new ReflectionFactoryImpl();
        KClass cdgPassType = reflectionFactory.getOrCreateKotlinClass(ControlDependenceGraphPass.class);
        TranslationConfiguration configuration = new TranslationConfiguration.Builder().sourceLocations(files)
                .defaultPasses()
                .registerPass(cdgPassType)
                .registerLanguage(new CLanguage())
                .registerLanguage(new CPPLanguage()).build();

        TranslationManager translationManager = TranslationManager.builder().config(configuration).build();
        TranslationResult translationResult = translationManager.analyze().get();
        return translationResult;
    }
}
