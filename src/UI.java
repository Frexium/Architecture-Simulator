import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UI extends Frame{
    Map<String, Integer> variables = new HashMap<>();
    Map<String, Integer> registers = new HashMap<>();
    java.util.List<String> lines = new ArrayList<>();

    static int counter;
    JButton loadFile = new JButton("Load file");
    JButton button = new JButton("Simulate");
    JButton simulate = new JButton("Simulate by steps");
    JButton stopSim = new JButton("Stop simulation");
    JButton next = new JButton("Next");

    Label fileInfo = new Label("File info :");
    Label fileName = new Label("File name : ");
    Label nameValue = new Label("code.mod7");
    Label fileStatus = new Label("File status : ");
    Label statusValue = new Label("status");
    Label variablesLabel = new Label("Variable");
    Label stack = new Label("Stack");
    Label code = new Label("Code");
    Label registersLabel = new Label("Registers");
    Label t0 = new Label("T0 : ");
    Label t1 = new Label("T1 : ");
    Label t2 = new Label("T2 : ");
    Label t3  = new Label("T3 : ");
    Label t0Value = new Label("0");
    Label t1Value = new Label("0");
    Label t2Value = new Label("0");
    Label t3Value = new Label("0");
    Label line = new Label("Line");
    Label lineValue = new Label("");

    List codeList = new List();
    List variableList = new List();
    List stackList = new List();
    UI(){
        loadFile.setBounds(353,310,200,25);
        button.setBounds(353,340,200,25);
        simulate.setBounds(353,370,200,25);
        stopSim.setBounds(353,400,200,25);
        next.setBounds(180,133,100,25);

        fileInfo.setBounds(40,40, 100, 25);
        fileName.setBounds(30,70,100,25);
        nameValue.setBounds(130,70,100,25);
        fileStatus.setBounds(30,105,100,25);
        statusValue.setBounds(130,105,100,25);
        variablesLabel.setBounds(358,40,100,25);
        stack.setBounds(358,173,100,25);
        code.setBounds(30,133,100,25);
        registersLabel.setBounds(30,280,100,25);
        t0.setBounds(103,310,100,25);
        t1.setBounds(103,340,100,25);
        t2.setBounds(103,370,100,25);
        t3.setBounds(103,400,100,25);
        t0Value.setBounds(202,310,100,25);
        t1Value.setBounds(202,340,100,25);
        t2Value.setBounds(202,370,100,25);
        t3Value.setBounds(202,400,100,25);
        line.setBounds(30, 240, 100, 25);
        lineValue.setBounds(130, 240, 200,25);

        codeList.setBounds(29,160,274,78);
        variableList.setBounds(353,70,200,100);
        stackList.setBounds(353,200,200,100);

        Frame f = new Frame();

        f.add(loadFile);
        f.add(button);
        f.add(simulate);
        f.add(stopSim);
        f.add(next);

        f.add(fileInfo);
        f.add(fileName);
        f.add(nameValue);
        f.add(fileStatus);
        f.add(statusValue);
        f.add(variablesLabel);
        f.add(stack);
        f.add(code);
        f.add(registersLabel);
        f.add(t0);
        f.add(t1);
        f.add(t2);
        f.add(t3);
        f.add(t0Value);
        f.add(t1Value);
        f.add(t2Value);
        f.add(t3Value);
        f.add(line);
        f.add(lineValue);

        f.add(codeList);
        f.add(variableList);
        f.add(stackList);

        next.setVisible(false);
        next.setEnabled(false);
        stopSim.setEnabled(false);
        simulate.setEnabled(false);
        button.setEnabled(false);

        f.setSize(600,450);
        f.setLayout(null);
        f.setVisible(true);

        loadFile.addActionListener(e -> {
            statusValue.setText("Loading");
            try {
                readFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            simulate.setEnabled(true);
            button.setEnabled(true);
        });
        simulate.addActionListener(e -> {
            try {
                readFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            statusValue.setText("Running...");
            next.setVisible(true);
            next.setEnabled(true);
            stopSim.setEnabled(true);
            counter = codeList.getItemCount();
            try {
                read(codeList.getItem(0));
                lineValue.setText(codeList.getItem(0));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            codeList.select(0);
            counter -= 1;
        });
        next.addActionListener(e -> {
            if(counter >= 0){
                try {
                    read(codeList.getItem(codeList.getItemCount() - counter));
                    lineValue.setText(codeList.getItem(codeList.getItemCount() - counter));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                codeList.select(codeList.getItemCount() - counter);
                counter -= 1;
            }else{
                next.setEnabled(false);
                next.setVisible(false);
            }
        });
        stopSim.addActionListener(e -> {
            statusValue.setText("Stopped");
            counter = codeList.getItemCount();
            next.setVisible(false);
            next.setEnabled(false);
            lineValue.setText("");
            stopSim.setEnabled(false);
        });
        button.addActionListener(e -> {
            statusValue.setText("Running...");
            counter = codeList.getItemCount();
            codeList.select(codeList.getItemCount() - 1);
            while(counter > 0){
                try {
                    read(codeList.getItem(codeList.getItemCount() - counter));
                    lineValue.setText(codeList.getItem(codeList.getItemCount() - counter));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                counter -= 1;
            }
            statusValue.setText("Stopped");
        });
    }

    public void readFile() throws IOException {
        codeList.removeAll();
        variableList.removeAll();
        stackList.removeAll();
        registers.clear();
        registers.put("T0", 0);
        registers.put("T1", 0);
        registers.put("T2", 0);
        registers.put("T3", 0);
        File file = new File("code.mod7");
        lines = Files.readAllLines(file.toPath());

        int pos = 0;
        char firstLetter;
        for(String line : lines) {
            if (line.length() < 1) {
                firstLetter = '!';
            } else {
                firstLetter = line.charAt(0);
            }
            if(firstLetter == '#'){
                switch (line) {
                    case ("#DATA"):
                        pos = 1;
                        break;
                    case ("#CODE"):
                        pos = 2;
                        break;
                }
            }else if(firstLetter != '!'){
                switch (pos){
                    case(1):
                        String[] data_parts = line.split(" ");
                        if(data_parts.length == 2){
                            String name = data_parts[0].trim();
                            int value = Integer.parseInt(data_parts[1].trim());
                            variables.put(name, value);
                        }
                        break;
                    case(2):
                        codeList.add(line);
                        break;
                }
            }
        }
        for(Map.Entry<String, Integer> entry : variables.entrySet()){
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            variableList.add(key + " = " + value);
        }
        counter = codeList.getItemCount();
        statusValue.setText("Loaded");
        updateValues();
    }
    public void read(String s) throws IOException {
        String[] code_parts = s.split(" ");
        String action = code_parts[0].trim();
        String[] data = new String[3];
        switch (code_parts.length){
            case(1):
                break;
            case(2):
                data[0] = code_parts[1];
                break;
            case(3):
                data[0] = code_parts[1];
                data[1] = code_parts[2];
                break;
            case(4):
                data[0] = code_parts[1];
                data[1] = code_parts[2];
                data[2] = code_parts[3];
                break;
        }
        boolean isRegister0, isRegister1;
        if(data[0] != null){
            isRegister0 = data[0].equals("T0") || data[0].equals("T1") || data[0].equals("T2") || data[0].equals("T3");
        }else{
            isRegister0 = false;
        }
        if(data[1] != null) {
            isRegister1 = data[1].equals("T0") || data[1].equals("T1") || data[1].equals("T2") || data[1].equals("T3");
        }else{
            isRegister1 = false;
        }
        switch(action){
            case("LDA"):
                if(isRegister0){
                    if(isRegister1){
                        registers.put(data[0], registers.get(data[1]));
                        updateValues();
                        break;
                    }else if(isInteger(data[1])){
                        assert data[1] != null;
                        registers.put(data[0], Integer.parseInt(data[1]));
                        updateValues();
                        break;
                    }else{
                        registers.put(data[0], variables.get(data[1]));
                        updateValues();
                        break;
                    }
                }
                break;
            case("STR"):
                if(!isRegister0){
                    if(isRegister1){
                        variables.put(data[0], registers.get(data[1]));
                    }else{
                        assert data[1] != null;
                        variables.put(data[0], Integer.parseInt(data[1]));
                    }
                    updateValues();
                    break;
                }
                break;
            case("AND"):
                if(isRegister0){
                    if(isRegister1){
                        registers.put(data[0], registers.get(data[1]) & registers.get(data[0]));
                        updateValues();
                        break;
                    }else if(isInteger(data[1])){
                        assert data[1] != null;
                        registers.put(data[0], Integer.parseInt(data[1]) & registers.get(data[0]));
                        updateValues();
                        break;
                    }else{
                        registers.put(data[0], variables.get(data[1]) & registers.get(data[0]));
                        updateValues();
                        break;
                    }
                }
                break;
            case("OR"):
                if(isRegister0){
                    if(isRegister1){
                        registers.put(data[0], registers.get(data[1]) | registers.get(data[0]));
                        updateValues();
                        break;
                    }else if(isInteger(data[1])){
                        assert data[1] != null;
                        registers.put(data[0], Integer.parseInt(data[1]) | registers.get(data[0]));
                        updateValues();
                        break;
                    }else{
                        registers.put(data[0], variables.get(data[1]) | registers.get(data[0]));
                        updateValues();
                        break;
                    }
                }
                break;
            case("ADD"):
                if(isRegister0){
                    if(isRegister1){
                        registers.put(data[0], registers.get(data[1]) + registers.get(data[0]));
                        updateValues();
                        break;
                    }else if(isInteger(data[1])){
                        assert data[1] != null;
                        registers.put(data[0], Integer.parseInt(data[1]) + registers.get(data[0]));
                        updateValues();
                        break;
                    }else{
                        registers.put(data[0], variables.get(data[1]) + registers.get(data[0]));
                        updateValues();
                        break;
                    }
                }
                break;
            case("SUB"):
                if(isRegister0){
                    if(isRegister1){
                        registers.put(data[0], registers.get(data[1]) - registers.get(data[0]));
                        updateValues();
                        break;
                    }else if(isInteger(data[1])){
                        assert data[1] != null;
                        registers.put(data[0], Integer.parseInt(data[1]) - registers.get(data[0]));
                        updateValues();
                        break;
                    }else{
                        registers.put(data[0], variables.get(data[1]) - registers.get(data[0]));
                        updateValues();
                        break;
                    }
                }
                break;
            case("DIV"):
                if(isRegister0){
                    if(isRegister1){
                        registers.put(data[0], registers.get(data[1]) / registers.get(data[0]));
                        updateValues();
                        break;
                    }else if(isInteger(data[1])){
                        assert data[1] != null;
                        registers.put(data[0], Integer.parseInt(data[1]) / registers.get(data[0]));
                        updateValues();
                        break;
                    }else{
                        registers.put(data[0], variables.get(data[1]) / registers.get(data[0]));
                        updateValues();
                        break;
                    }
                }
                break;
            case("MUL"):
                if(isRegister0){
                    if(isRegister1){
                        registers.put(data[0], registers.get(data[1]) * registers.get(data[0]));
                        updateValues();
                        break;
                    }else if(isInteger(data[1])){
                        assert data[1] != null;
                        registers.put(data[0], Integer.parseInt(data[1]) * registers.get(data[0]));
                        updateValues();
                        break;
                    }else{
                        registers.put(data[0], variables.get(data[1]) * registers.get(data[0]));
                        updateValues();
                        break;
                    }
                }
                break;
            case("MOD"):
                if(isRegister0) {
                    if (isRegister1) {
                        registers.put(data[0], registers.get(data[1]) % registers.get(data[0]));
                        updateValues();
                        break;
                    } else if (isInteger(data[1])) {
                        assert data[1] != null;
                        registers.put(data[0], Integer.parseInt(data[1]) % registers.get(data[0]));
                        updateValues();
                        break;
                    } else {
                        registers.put(data[0], variables.get(data[1]) % registers.get(data[0]));
                        updateValues();
                        break;
                    }
                }
                break;
            case("PUSH"):
                if(isRegister0){
                    stackList.add(String.valueOf(registers.get(data[0])));
                    break;
                }else if(isInteger(data[0])){
                    stackList.add(data[0]);
                    break;
                }else{
                    stackList.add(String.valueOf(variables.get(data[0])));
                    break;
                }
            case("POP"):
                if(isRegister0){
                    int lastOne = stackList.getItemCount();
                    registers.put(data[0], Integer.valueOf(stackList.getItem(lastOne)));
                }
                break;
            case("NOT"):
                if(isRegister0){
                    registers.put(data[0], ~(registers.get(data[0])));
                    updateValues();
                    break;
                }
                break;
            case("INC"):
                if(isRegister0){
                    registers.put(data[0], +1);
                    updateValues();
                    break;
                }
                break;
            case("DEC"):
                if(isRegister0){
                    registers.put(data[0], -1);
                    updateValues();
                    break;
                }
                break;
            case("JMP"):
                int i=lines.size();
                for(String line : lines){
                    if(Objects.equals(line, data[0])){
                        counter = i;
                    }else{
                        i -= 1;
                    }
                }
                break;
            case("BEQ"):
                if(isRegister0){
                    if(isRegister1){
                        if(registers.get(data[0]).equals(registers.get(data[1]))){
                            int a = lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = a;
                                }else{
                                    a -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        if(registers.get(data[0]).equals(variables.get(data[1]))){
                            int b = lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = b;
                                }else{
                                    b -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[1] != null;
                        if(registers.get(data[0]).equals(Integer.parseInt(data[1]))){
                            int c = lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = c;
                                }else{
                                    c -= 1;
                                }
                            }
                            break;
                        }
                    }
                }else if(!isInteger(data[0])){
                    if(isRegister1){
                        if(variables.get(data[0]).equals(registers.get(data[1]))){
                            int d =lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = d;
                                }else{
                                    d -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        if(variables.get(data[0]).equals(variables.get(data[1]))){
                            int e =lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = e;
                                }else{
                                    e -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[1] != null;
                        if(variables.get(data[0]).equals(Integer.parseInt(data[1]))){
                            int f =lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = f;
                                }else{
                                    f -= 1;
                                }
                            }
                            break;
                        }
                    }
                }else{
                    if(isRegister1){
                        assert data[0] != null;
                        if(Integer.parseInt(data[0]) == (registers.get(data[1]))){
                            int g=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = g;
                                }else{
                                    g -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        assert data[0] != null;
                        if(Integer.parseInt(data[0]) == (variables.get(data[1]))){
                            int f=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = f;
                                }else{
                                    f -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[0] != null;
                        assert data[1] != null;
                        if(Integer.parseInt(data[0]) == (Integer.parseInt(data[1]))){
                            int g=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = g;
                                }else{
                                    g -= 1;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            case("BNE"):
                if(isRegister0){
                    if(isRegister1){
                        if(!registers.get(data[0]).equals(registers.get(data[1]))){
                            int h=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = h;
                                }else{
                                    h -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        if(!registers.get(data[0]).equals(variables.get(data[1]))){
                            int j=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j;
                                }else{
                                    j -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[1] != null;
                        if(!registers.get(data[0]).equals(Integer.parseInt(data[1]))){
                            int k=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = k;
                                }else{
                                    k -= 1;
                                }
                            }
                            break;
                        }
                    }
                }else if(!isInteger(data[0])){
                    if(isRegister1){
                        if(!variables.get(data[0]).equals(registers.get(data[1]))){
                            int m=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = m;
                                }else{
                                    m -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        if(!variables.get(data[0]).equals(variables.get(data[1]))){
                            int n =lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = n;
                                }else{
                                    n -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[1] != null;
                        if(!variables.get(data[0]).equals(Integer.parseInt(data[1]))){
                            int o=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = o;
                                }else{
                                    o -= 1;
                                }
                            }
                            break;
                        }
                    }
                }else{
                    if(isRegister1){
                        assert data[0] != null;
                        if(Integer.parseInt(data[0]) != (registers.get(data[1]))){
                            int p=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = p;
                                }else{
                                    p -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        assert data[0] != null;
                        if(Integer.parseInt(data[0]) != (variables.get(data[1]))){
                            int i2=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = i2;
                                }else{
                                    i2 -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[0] != null;
                        assert data[1] != null;
                        if(Integer.parseInt(data[0]) != (Integer.parseInt(data[1]))){
                            int i3=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = i3;
                                }else{
                                    i3 -= 1;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            case("BBG"):
                if(isRegister0){
                    if(isRegister1){
                        if(registers.get(data[0]) > (registers.get(data[1]))){
                            int i4=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = i4;
                                }else{
                                    i4 -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        if(registers.get(data[0]) > (variables.get(data[1]))){
                            int i5=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = i5;
                                }else{
                                    i5 -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[1] != null;
                        if(registers.get(data[0]) > (Integer.parseInt(data[1]))){
                            int i6=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = i6;
                                }else{
                                    i6 -= 1;
                                }
                            }
                            break;
                        }
                    }
                }else if(!isInteger(data[0])){
                    if(isRegister1){
                        if(variables.get(data[0]) > (registers.get(data[1]))){
                            int i7=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = i7;
                                }else{
                                    i7 -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        if(variables.get(data[0]) > (variables.get(data[1]))){
                            int i8=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = i8;
                                }else{
                                    i8 -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[1] != null;
                        if(variables.get(data[0]) > (Integer.parseInt(data[1]))){
                            int i9=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = i9;
                                }else{
                                    i9 -= 1;
                                }
                            }
                            break;
                        }
                    }
                }else{
                    if(isRegister1){
                        assert data[0] != null;
                        if(Integer.parseInt(data[0]) > (registers.get(data[1]))){
                            int j1=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j1;
                                }else{
                                    j1 -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        assert data[0] != null;
                        if(Integer.parseInt(data[0]) > (variables.get(data[1]))){
                            int j2=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j2;
                                }else{
                                    j2 -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[0] != null;
                        assert data[1] != null;
                        if(Integer.parseInt(data[0]) > (Integer.parseInt(data[1]))){
                            int j3=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j3;
                                }else{
                                    j3 -= 1;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            case("BSM"):
                if(isRegister0){
                    if(isRegister1){
                        if(registers.get(data[0]) < (registers.get(data[1]))){
                            int j4=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j4;
                                }else{
                                    j4 -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        if(registers.get(data[0]) < (variables.get(data[1]))){
                            int j5=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j5;
                                }else{
                                    j5 -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[1] != null;
                        if(registers.get(data[0]) < (Integer.parseInt(data[1]))){
                            int j6=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j6;
                                }else{
                                    j6 -= 1;
                                }
                            }
                            break;
                        }
                    }
                }else if(!isInteger(data[0])){
                    if(isRegister1){
                        if(variables.get(data[0]) < (registers.get(data[1]))){
                            int j7=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j7;
                                }else{
                                    j7 -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        if(variables.get(data[0]) < (variables.get(data[1]))){
                            int j8=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j8;
                                }else{
                                    j8 -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[1] != null;
                        if(variables.get(data[0]) < (Integer.parseInt(data[1]))){
                            int j9=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = j9;
                                }else{
                                    j9 -= 1;
                                }
                            }
                            break;
                        }
                    }
                }else{
                    if(isRegister1){
                        assert data[0] != null;
                        if(Integer.parseInt(data[0]) < (registers.get(data[1]))){
                            int a1=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = a1;
                                }else{
                                    a1 -= 1;
                                }
                            }
                            break;
                        }
                    }else if(!isInteger(data[1])){
                        assert data[0] != null;
                        if(Integer.parseInt(data[0]) < (variables.get(data[1]))){
                            int a2=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = a2;
                                }else{
                                    a2 -= 1;
                                }
                            }
                            break;
                        }
                    }else{
                        assert data[0] != null;
                        assert data[1] != null;
                        if(Integer.parseInt(data[0]) < (Integer.parseInt(data[1]))){
                            int a3=lines.size();
                            for(String line : lines){
                                if(Objects.equals(line, data[0])){
                                    counter = a3;
                                }else{
                                    a3 -= 1;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            case("HLT"):
                stopSim.setEnabled(false);
                next.setEnabled(false);
                next.setVisible(false);
                statusValue.setText("Stopped");
                lineValue.setText("");
                break;
        }
    }


    public static boolean isInteger(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void updateValues(){
        t0Value.setText(String.valueOf(registers.get("T0")));
        t1Value.setText(String.valueOf(registers.get("T1")));
        t2Value.setText(String.valueOf(registers.get("T2")));
        t3Value.setText(String.valueOf(registers.get("T3")));
        variableList.removeAll();
        for(Map.Entry<String, Integer> entry : variables.entrySet()){
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            variableList.add(key + " = " + value);
        }
    }
}

