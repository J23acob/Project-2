% smoother.m
% I read an (x,y) CSV, replace each y with the mean of its neighbours inside
% a ±window, save the new points, and plot original vs. smoothed curves.

function smoother(window, inFile, outFile)

  % Defaults
  if nargin < 1, window  = 5;                  end   % window radius
  if nargin < 2, inFile  = "plot_data.csv";    end   % input CSV
  if nargin < 3, outFile = "smoothed_data.csv";end   % output CSV

  % Load data (two columns: x | y)
  raw = csvread(inFile);
  x = raw(:,1);
  y = raw(:,2);
  n = numel(y);

  % Moving-average smoothing
  y_smooth = zeros(size(y));
  for i = 1:n
    left  = max(1, i - window);
    right = min(n, i + window);
    y_smooth(i) = mean(y(left:right));
  end

  % Save results
  csvwrite(outFile, [x, y_smooth]);
  printf("CSV written: %s  (%d rows, window ±%d)\n", outFile, n, window);

  % Plot comparison
  figure;
  plot(x, y,        'k.-', "displayname", "original");
  hold on;
  plot(x, y_smooth, 'r-',  "linewidth", 1.5, "displayname", "smoothed");
  grid on;
  xlabel('x'); ylabel('y');
  title(sprintf("Moving-average smoothing (window ±%d)", window));
  legend show;

end

