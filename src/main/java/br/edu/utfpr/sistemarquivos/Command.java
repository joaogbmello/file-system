package br.edu.utfpr.sistemarquivos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {

            if (Files.isRegularFile(path)) {
                throw new UnsupportedOperationException("Error - LIST");
            }

            final var fileList = Files.list(path).toList();
            for (var x : fileList) {
                System.out.println(x);
            }
            return path;
        }
    },
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) throws UnsupportedOperationException {
            Path newPath = path.resolve(parameters[1]);
            //no enunciado da questão não é mencionado para abrir somente TXT, mas no seu vídeo é falado
            if (!Files.isRegularFile(newPath) || !parameters[1].toLowerCase().endsWith(".txt")) {
                throw new UnsupportedOperationException("Error, only TXT files - SHOW");
            }
            FileReader.read(String.valueOf(newPath));
            return path;
        }

    },
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {
            path = path.getParent();
            if (path == null) {
                throw new UnsupportedOperationException("Error Root - BACK");
            }

            return path;
        }
    },
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {
            System.out.println("Opening Folder...");
            path = path.resolve(parameters[1]);
            if (!Files.isDirectory(path)) {
                throw new UnsupportedOperationException("Error, only directorys - OPEN");
            }
            System.out.println("Done!");

            return path;
        }
    },
    DETAIL() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) throws IOException {
            Path newPath = path.resolve(parameters[1]);
            if (Files.isDirectory(path)) {
                System.out.println("[ Folder Details ]");
            } else {
                System.out.println("[ File  Details ]");
            }

            BasicFileAttributes attributes = Files.readAttributes(newPath, BasicFileAttributes.class);
            System.out.println("Size: "+attributes.size()+"\n"+"Date Created On "+ attributes.creationTime()+"\n"+"Last Access time "+attributes.lastAccessTime());
            return path;
        }
    },
    EXIT() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            System.out.print("Exiting...");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }
}
