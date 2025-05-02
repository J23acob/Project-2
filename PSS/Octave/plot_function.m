% plot_function.m
% I sample x from -10 to 10, compute y = x^2, plot the curve, and save the
% data to plot_data.csv.

function plot_function()

  % Basic settings: domain, step size, and output file name
  x_start = -10;
  x_end   =  10;
  step    =  0.5;
  csv_out = "plot_data.csv";

  % Build vectors and evaluate the function
  x = x_start:step:x_end;
  y = x .^ 2;          % change this line to try a new function

  % Quick plot so I can see the shape right away
  figure;
  plot(x, y, 'o-');
  grid on;
  xlabel('x');
  ylabel('y');
  title('y = x^2');

  % Write the (x,y) pairs to CSV for other tools
  data = [x.'  y.'];
  csvwrite(csv_out, data);
  printf("CSV written: %s  (%d rows)\n", csv_out, rows(data));

end

