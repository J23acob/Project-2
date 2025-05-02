% salter.m
% I load an (x,y) CSV, add uniform noise ±range to each y, save the noisy
% data to a new CSV, and plot original vs. salted curves.

function salter(range, inFile, outFile, rngSeed)

  % Default arguments
  if nargin < 1, range   = 2.0;              end   % noise amplitude
  if nargin < 2, inFile  = "plot_data.csv";  end   % input file
  if nargin < 3, outFile = "salted_data.csv";end   % output file
  if nargin == 4, rng(rngSeed);              end   % fixed seed if given

  % Read the source CSV (two columns: x | y)
  raw = csvread(inFile);
  x = raw(:,1);
  y = raw(:,2);
  n = numel(y);

  % Create uniform noise in (–range, +range) and add it to y
  noise     = (rand(n,1) * 2*range) - range;
  y_salted  = y + noise;

  % Save the salted series
  csvwrite(outFile, [x, y_salted]);
  printf("CSV written: %s  (%d rows, ±%.2f noise)\n", outFile, n, range);

  % Show both curves side by side
  figure;
  plot(x, y,        'b.-', "displayname", "original");
  hold on;
  plot(x, y_salted, 'ro',  "markerfacecolor", "r", ...
                     "displayname", "salted");
  grid on;
  xlabel('x'); ylabel('y');
  title(sprintf("Uniform noise ±%.2f", range));
  legend show;

end

