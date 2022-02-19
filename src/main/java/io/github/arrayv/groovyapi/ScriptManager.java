package io.github.arrayv.groovyapi;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

public final class ScriptManager {
    public final class ScriptThread extends Thread {
        private final File path;
        private final Script script;

        private ScriptThread(File path, Script script) {
            super(script::run, "Script-" + getScriptName(path));
            this.path = path;
            this.script = script;
        }

        public File getPath() {
            return path;
        }

        public Script getScript() {
            return script;
        }
    }

    private static final File INSTALLED_SCRIPTS_ROOT = new File("scripts");

    private final GroovyShell shell;
    private Map<String, Script> installedScripts;

    public ScriptManager() {
        final CompilerConfiguration compilerConfig = new CompilerConfiguration();
        compilerConfig.addCompilationCustomizers(
            new ImportCustomizer()
                .addStarImports("io.github.arrayv.sortdata")
                .addStarImports("io.github.arrayv.groovyapi")
                .addStaticStars("io.github.arrayv.groovyapi.GroovyLocals")
        );
        compilerConfig.getClasspath().add(INSTALLED_SCRIPTS_ROOT.getPath());
        this.shell = new GroovyShell(compilerConfig);
        this.installedScripts = null;
    }

    public GroovyShell getGroovyShell() {
        return shell;
    }

    public Script loadScript(File path) throws IOException {
        Script script = shell.parse(path);
        script.run();
        return script;
    }

    public ScriptThread runInThread(File path) throws IOException {
        Script script = shell.parse(path);
        ScriptThread thread = new ScriptThread(path, script);
        thread.start();
        return thread;
    }

    public static String getScriptName(File path) {
        String name = path.getName();
        return name.substring(0, name.length() - 7);
    }

    public Map<String, Script> loadInstalledScripts() throws IOException {
        if (installedScripts != null) {
            throw new IllegalStateException("Cannot load installed scripts more than once (i.e. you are not a privileged caller)");
        }
        if (!INSTALLED_SCRIPTS_ROOT.exists()) {
            INSTALLED_SCRIPTS_ROOT.mkdir();
            return installedScripts = Collections.emptyMap(); // There are definitely no scripts in a newly created directory
        }
        installedScripts = new HashMap<>();
        for (File subFile : INSTALLED_SCRIPTS_ROOT.listFiles()) {
            if (!subFile.isFile() || !subFile.getPath().endsWith(".groovy")) {
                continue;
            }
            String name = subFile.getName();
            name = name.substring(0, name.length() - 7);
            installedScripts.put(name, loadScript(subFile));
        }
        return installedScripts;
    }

    public Map<String, Script> getInstalledScripts() {
        if (installedScripts == null) {
            throw new Error("Cannot return installed scripts before they're loaded");
        }
        return installedScripts;
    }
}
