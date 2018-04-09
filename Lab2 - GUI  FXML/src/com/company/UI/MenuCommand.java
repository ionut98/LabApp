package com.company.UI;


import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MenuCommand implements Command{

    private String menuName;
    private Map<String, Command> menuMap = new TreeMap<>();

    public MenuCommand(String menuName) {
        this.menuName = menuName;
    }

    @Override
    public void execute() {
        this.menuMap.keySet().forEach((x)->{
            System.out.println(x);
        });

    }

    public void addCommand(String desc, Command c){
        this.menuMap.put(desc,c);
    }

    public List<Command> getCommands(){return (List)this.menuMap.values().stream().collect(Collectors.toList());}

    public String getMenuName(){
        return this.menuName;
    }

}
