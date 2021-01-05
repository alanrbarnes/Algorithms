let default_number_of_points = 50;
let display_point_text_chkbx;
let point_outputarea;
let point_inputarea;
let canvas;

let points = [];

const window_width_offset = 0;
const window_height_offset = 400;

let points_connected = false;

class Point{
    constructor(X, Y){
        this.x = int(X);
        this.y = int(Y);
    }

    to_string() {
        return this.x + " " + this.y;
    }

    display() {
        point(this.x, this.y);
    }

    equals(other) {
        return this.x === other.x && this.y === other.y;
    }

    distance(other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }
}

function draw_points() {
    strokeWeight(5);
    if(points.length != 0) {
        points.forEach(p =>{
            point(p.x, p.y);
        });
    }
    strokeWeight(1);
}

function draw_all_points_text() {
    if(points.length != 0) {
        draw_points();
        strokeWeight(0);
        points.forEach(p => {
            text("(" + p.x + ", " + p.y + ")", p.x-7, p.y-7);
        });
    }
}

function draw_inputarea_points_text() {
    draw_points();
    let input_points = point_inputarea.elt.value;
    input_points = input_points.split("\n");
    if(input_points.length >= 1){
        input_points.forEach(v => {
            if(v != "") {
                strokeWeight(0);
                let p_split = v.split(" ");
                let p = new Point(p_split[0], p_split[1]);
                text("(" + p.x + ", " + p.y + ")", p.x-7, p.y-7);
            }
        });
    }
}

function make_lines(point_set) {
    var leftmost_point = null;
    var rightmost_point = null;

    let input_points_arr = [];
    let input_points = point_inputarea.elt.value.split("\n");
    input_points = input_points.forEach(p => {
        if(p != "") {
            p = p.split(" ");
            if(p.length < 2 || p.length > 2){
                return;
            }
            input_points_arr.push(new Point(int(p[0]), int(p[1])));
        }
    });
    
    if(input_points_arr.length == 0){
        return;
    }

    input_points_arr.forEach(p => {
        if(leftmost_point == null || leftmost_point.x > p.x) {
            leftmost_point = p;
        }

        if(rightmost_point == null || rightmost_point.x < p.x) {
            rightmost_point = p;
        }
    });
    let m = (leftmost_point.y - rightmost_point.y) / (leftmost_point.x - rightmost_point.x);
    let b = leftmost_point.y - (m * leftmost_point.x);

    let above = [];
    let below = [];

    input_points_arr.forEach(p => {
        let y = m * p.x + b;
        if((p.x != leftmost_point.x && p.y != leftmost_point.y) || (p.x != rightmost_point.x && p.y != rightmost_point.y)){
            if(y < p.y) {
                above.push(p);
            }else if(y > p.y) {
                below.push(p);
            }
        }
    });

    above = above.sort(function(p1, p2){return p1.x - p2.x;});
    below = below.sort(function(p1, p2){return p2.x - p1.x;});

    let polygon = [leftmost_point];
    polygon = polygon.concat(above);
    polygon.push(rightmost_point);
    polygon = polygon.concat(below);

    let last_point = null;
    let first_point = null;
    polygon.forEach(p => {
        if(last_point == null) {
            last_point = p;
            first_point = p;
        } else {
            line(p.x, p.y, last_point.x, last_point.y);
            last_point = p;
        }
    });
    line(last_point.x, last_point.y, first_point.x, first_point.y);
}


function compare(a, b) {
    return a - b;
}

function connect_points() {
    let input_points = point_inputarea.elt.value;
    let did_work = false;
    input_points = input_points.split("\n");

    let connected_points = [];
    if(input_points.length >= 1){
        input_points.forEach(v => {
            if(v != "") {
                strokeWeight(1);
                let p_split = v.split(" ");
                let p = new Point(p_split[0], p_split[1]);
                connected_points.push(p);
                did_work = true;
            }
        });
        make_lines(connected_points);
    }
    return did_work;
}

function generate_points(count, minx, miny, maxx, maxy, x_offset=0, y_offset=0) {
    let result = [];

    for(let p = 0; p < count; p++) {
        var x = int(random(minx, maxx) + x_offset);
        var y = int(random(miny, maxy) + y_offset);
        result.push(new Point(x, y));
    }

    return result;
}

function update_output_area() {
    let result = "";
    points.forEach(p => {
        result += p.to_string() + "\n";
    });
    point_outputarea.elt.value = result;
}

function doMouseClicked() {
    let mPoint = new Point(mouseX, mouseY);
    let input_area = [];

    if(point_inputarea.elt.value != "") {
        point_inputarea.elt.value.split("\n").forEach(line => {
            line = line.split(" ");
            if(line.length == 2) {
                let x = int(line[0]);
                let y = int(line[1]);
                input_area.push(new Point(x, y));
            }
        });
    }

    point_inputarea.elt.value = "";

    let should_add = true;
    let newPoint = null;
    points.forEach(p => {
        if(p.distance(mPoint) <= 3){
            newPoint = p;
        }
    });
    if(newPoint != null) {
        if(input_area.length == 0){
            point_inputarea.elt.value = newPoint.to_string() + "\n";
        } else {
            input_area.forEach(p => {
                if(p.equals(newPoint)){
                    should_add = false;
                } else {
                    point_inputarea.elt.value += p.to_string() + "\n";
                }
            });
            if(should_add){
                point_inputarea.elt.value += newPoint.to_string() + "\n";
            }
        }
    } else {
        input_area.forEach(p => {
            point_inputarea.elt.value += p.to_string() + "\n";
        });
    }
}

function setup() {
    canvas = createCanvas(windowWidth-window_width_offset, windowHeight-window_height_offset);
    canvas.parent('main-canvas');
    canvas.mouseClicked(doMouseClicked);

    let user_in = createInput(str(default_number_of_points));
    user_in.parent('buttons');

    let generate_button = createButton('Generate Points').id("functional-button");
    generate_button.parent('buttons');
    generate_button.mousePressed((function () {
        point_inputarea.elt.value = "";
        let num_of_points = int(user_in.elt.value);
        if(num_of_points <= 2) {
            alert("Number of points must be greater than 2.  Setting to default: " + default_number_of_points);
            num_of_points = default_number_of_points;
        }
        points = generate_points(num_of_points, width*.1, -height/3, width*.9, height/3+75, 0, (height/2)-(height*.1));
        update_output_area();
        points_connected = false;
    }));

    let connect_points_btn = createButton('Make Polygon').id('functional-button');
    connect_points_btn.parent('buttons');
    connect_points_btn.mousePressed((function () {
        points_connected = connect_points();
    }));

    createDiv('<p><b>Display (x, y) Above Points</b></p>').parent('buttons').html();
    display_point_text_chkbx = createRadio();
    display_point_text_chkbx.option('None', 1);
    display_point_text_chkbx.option('All Points', 2);
    display_point_text_chkbx.option('Input Points', 3);
    display_point_text_chkbx.style('width', '100%');
    display_point_text_chkbx.parent('buttons');
    display_point_text_chkbx.value('1');

    createDiv("<p><b>Generated Points</b></p>").parent('point-io').id('p-out').html();
    point_outputarea = createElement('textarea');
    point_outputarea.attribute("readonly", "readonly");
    point_outputarea.parent('p-out');

    createDiv("<p><b>Points input</b></p>").parent('point-io').id('p-in').html();
    point_inputarea = createElement('textarea');
    point_inputarea.parent('p-in');
}

function draw() {
    background(255);
    strokeWeight(1);
    line(0, 0, width, 0);
    line(0, height, 0, 0);
    line(0, height, width, height);
    line(width, 0, width, height);

    if(display_point_text_chkbx.value() == 1) {
        draw_points();
    } else if(display_point_text_chkbx.value() == 2) {
        draw_all_points_text();
    } else if(display_point_text_chkbx.value() == 3) {
        draw_inputarea_points_text();
    }
    if(points_connected) {
        connect_points();
    }
}
