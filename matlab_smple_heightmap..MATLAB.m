//simple matlab program demonstrating importing a png and convering it to a heightmap with imagesc
img = imread('newhallArial.png');
grayImg = rgb2gray(img);
sharpenedImg = imsharpen(grayImg);
normalizedImg = mat2gray(sharpenedImg);
scaledImg = normalizedImg * 255;

minValue = min(normalizedImg(:));
maxValue = max(normalizedImg(:));
disp(['Min value: ', num2str(minValue)]);
disp(['Max value: ', num2str(maxValue)]);

figure;
imagesc(normalizedImg);
colormap('gray');
colorbar;
title('Normalized Image with imagesc');

imwrite(normalizedImg, 'heightmap.png');

