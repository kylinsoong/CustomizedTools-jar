package com.customized.tools.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.cl.validator.OptionValidator;
import org.jboss.aesh.cl.validator.OptionValidatorException;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.console.command.validator.ValidatorInvocation;

import com.customized.tools.commands.Validator.InputNotNullValidator;

@CommandDefinition(name="converter", description = "Convert value to a equivalent value by type")
public class ConverterCommand implements Command<CommandInvocation>{
    
    @Option(shortName = 'H', name = "help", hasValue = false,
            description = "display this help and exit")
    private boolean help;
        
    @Option(shortName = 't',
            name = "type", 
            description = "Converter Type, like H2D - Hexadecimal to Decimal Converter, D2H - Decimal to Hexadecimal Converter",
            defaultValue = {"H2D", "D2H"},
            validator = ConverterTypeOptionValidator.class)
    private String type;
    
    @Option(shortName = 'v',
            name = "value", 
            description = "Converter Value, the value that will be convert to a equivalent value",
            validator = InputNotNullValidator.class)
    private String value;

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {
        
        PrintStream out = commandInvocation.getShell().out();
        
        if(help) {
            out.println(commandInvocation.getHelpInfo("converter"));
            return CommandResult.SUCCESS;
        }
        
        switch(type) {
        case "H2D" :
            out.println(Integer.parseInt(value, 16));
            break;
        case "D2H" :
            int v = Integer.parseInt(value);
            out.println(Integer.toHexString(v));
            break;
        default: 
            break;
        }
        
        return CommandResult.SUCCESS;
    }
    
    private static class ConverterTypeOptionValidator implements OptionValidator<ValidatorInvocation<String, ConverterCommand>> {
        
        static Set<String> oppSet = new HashSet<>();
        
        static {
            oppSet.add("H2D");
            oppSet.add("D2H");
        }

        @Override
        public void validate(ValidatorInvocation<String, ConverterCommand> validatorInvocation)throws OptionValidatorException {

            String type = validatorInvocation.getValue();
            if(!oppSet.contains(type)){
                throw new OptionValidatorException("Validation failed, " + validatorInvocation.getValue() + " not a valid Type, expected: " + oppSet);
            }
        }
        
    }

}
