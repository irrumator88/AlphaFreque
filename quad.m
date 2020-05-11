cd /work/nimbus/jfiresto/byte-pairs
workingMatrix = csvread('quadPairsProgressive.txt');
top30 = maxk(workingMatrix(:),30)
minCell = top30(30)
[row,col] = find(workingMatrix > minCell-1)
row = row-1
col = col-1
dlmwrite('top30col.csv',col,'precision',5)
dlmwrite('top30row.csv',row,'precision',5)
[numRows,numCols] = size(workingMatrix)
totalSize = numRows.*numCols
imagesc(workingMatrix); colormap(hsv(18)); colorbar; axis tight; axis ij; lim = caxis
spy(workingMatrix, 'r', 1); xlabel(sprintf('Nonzero elements: %d/%d (%.2f%%)', nnz(workingMatrix), totalSize, nnz(workingMatrix)/totalSize.*100)); xticks(0:400:8000); xtickangle(45); yticks(0:400:8000); saveas(gcf,'sparse.pdf')
mesh(workingMatrix); colormap(hsv(18)); shading interp; colorbar; axis tight; axis ij; view(45,45); xticks(0:400:8000); xtickangle(45); yticks(0:400:8000); ytickangle(-45); saveas(gcf,'3D.pdf')

s = std(A(:,3))
m = mean(A(:,3))
B = sortrows(A,3,'descend');
C = B(:,3);
C(C <= m+s) = nan;
D = B(:,3);
D(D <= m+(s.*2)) = nan;
E = B(:,3);
E(E <= m+(s.*3)) = nan;

#alternative for triple coloring, don't forget to recapture vectors from B(:,3)
C((C <= m+s) | (C >= m+(s.*2))) = nan;
D((D <= m+(s.*2)) | (D >= m+(s.*3))) = nan;
E(E <= m+(s.*3)) = nan;

#to convert the nucleotide matrices into xyz-three-column vectors
[X,Y] = ind2sub(size(workingMatrix), 1:numel(workingMatrix));
XYZ = [X(:), Y(:), workingMatrix(:)];
E = XYZ(:,3);
E(E < 1) = nan;
#for bytepairs
imagesc(workingMatrix); colormap(hsv(18)); colorbar; axis tight; axis ij; xticks([1 64 128 192 256]); xticklabels({'0:AAAA','64:CAAA','128:GAAA','192:TAAA','255:TTTT'}); xtickangle(45); yticks([1 64 128 192 256]); yticklabels({'0:AAAA','64:CAAA','128:GAAA','192:TAAA','255:TTTT'}); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.5; lim = caxis; saveas(gcf,'heatmap.pdf')
spy(AraneaeEcoliProg3DevNorm, 'r', 8); xlabel(sprintf('Elements 3dev above norm: %d/%d (%.2f%%)', nnz(AraneaeEcoliProg3DevNorm), 65536, nnz(AraneaeEcoliProg3DevNorm)/65536.*100)); xticks(0:64:256); xtickangle(45); xticklabels({'0:AAAA','64:CAAA','128:GAAA','192:TAAA','255:TTTT'}); yticks(0:64:256); yticklabels({'0:AAAA','64:CAAA','128:GAAA','192:TAAA','255:TTTT'}); saveas(gcf,'sparse.pdf')
mesh(AraneaeEcoliProg); colormap(hsv(18)); shading interp; colorbar; axis tight; axis ij; view(45,30); xticks([1 64 128 192 256]); xticklabels({'0:AAAA','64:CAAA','128:GAAA','192:TAAA','255:TTTT'}); yticks([1 64 128 192 256]); yticklabels({'0:AAAA','64:CAAA','128:GAAA','192:TAAA','255:TTTT'}); ytickangle(-45); saveas(gcf,'3D.pdf')

#for sextets

sextetTicks = ([0 400 800 1200 1600 2000 2400 2800 3200 3600 4000 4400 4800 5200 5600 6000 6400 6800 7200 7600 7999]);
sextetLabels = ({'0:AAA','400:RAA','800:NAA','1200:DAA','1600:CAA','2000:QAA','2400:EAA','2800:GAA','3200:HAA','3600:IAA','4000:LAA','4400:KAA','4800:MAA','5200:FAA','5600:PAA','6000:SAA','6400:TAA','6800:WAA','7200:YAA','7600:VAA','7999:VVV'});

A = readmatrix('xyzSextet-all-toxins.txt');
B = A(:,3); s = std(B); m = mean(B); B(B <= m+(s.*3)) = nan;
scatter(A(:,1),A(:,2),A(:,3),'filled','r'); xticks(sextetTicks); xtickangle(45); xticklabels(sextetLabels); yticks(sextetTicks); yticklabels(sextetLabels); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; saveas(gcf,'all-toxins-sextet-scatter.pdf')
scatter(A(:,1),A(:,2),B,'filled','r'); xticks(sextetTicks); xtickangle(45); xticklabels(sextetLabels); yticks(sextetTicks); yticklabels(sextetLabels); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; saveas(gcf,'all-toxins-sextet-scatter-3dev.pdf')
scatter3(A(:,1),A(:,2),A(:,3),A(:,3),A(:,3),'filled'); axis ij; xticks(sextetTicks); xtickangle(-45); xticklabels(sextetLabels); yticks(sextetTicks); yticklabels(sextetLabels); ytickangle(45); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; saveas(gcf,'all-toxins-sextet-scatter3.pdf')
scatter3(A(:,1),A(:,2),B,B,B,'filled'); axis ij; xticks(sextetTicks); xtickangle(-45); xticklabels(sextetLabels); yticks(sextetTicks); yticklabels(sextetLabels); ytickangle(45); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; saveas(gcf,'all-toxins-sextet-scatter3-3dev.pdf')
stem3(A(:,1),A(:,2),A(:,3),'MarkerFaceColor','red','Color','k'); xticks(sextetTicks); xtickangle(45); xticklabels(sextetLabels); xtickangle(-45); yticks(sextetTicks); yticklabels(sextetLabels); ytickangle(45); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; axis ij; saveas(gcf,'all-toxins-sextet-stem3.pdf')
stem3(A(:,1),A(:,2),B,'MarkerFaceColor','red','Color','k'); xticks(sextetTicks); xtickangle(45); xticklabels(sextetLabels); xtickangle(-45); yticks(sextetTicks); yticklabels(sextetLabels); ytickangle(45); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; axis ij; saveas(gcf,'all-toxins-sextet-stem3-3dev.pdf')

#for octets

octetTicks = ([0 8000 16000 24000 32000 40000 48000 56000 64000 72000 80000 88000 96000 104000 112000 120000 128000 136000 144000 152000 159999]);
octetLabels = ({'0:AAAA','8000:RAAA','16000:NAAA','24000:DAAA','32000:CAAA','40000:QAAA','48000:EAAA','56000:GAAA','64000:HAAA','72000:IAAA','80000:LAAA','88000:KAAA','96000:MAAA','104000:FAAA','112000:PAAA','120000:SAAA','128000:TAAA','136000:WAAA','144000:YAAA','152000:VAAA','159999:VVVV'});

A = readmatrix('xyzOctet-all-toxins.txt');
B = A(:,3); s = std(B); m = mean(B); B(B <= m+(s.*3)) = nan;
scatter(A(:,1),A(:,2),A(:,3),'filled','r'); xticks(octetTicks); xtickangle(45); xticklabels(octetLabels); yticks(octetTicks); yticklabels(octetLabels); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; saveas(gcf,'all-toxins-octet-scatter.pdf')
scatter(A(:,1),A(:,2),B,'filled','r'); xticks(octetTicks); xtickangle(45); xticklabels(octetLabels); yticks(octetTicks); yticklabels(octetLabels); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; saveas(gcf,'all-toxins-octet-scatter-3dev.pdf')
scatter3(A(:,1),A(:,2),A(:,3),A(:,3),A(:,3),'filled'); axis ij; xticks(octetTicks); xtickangle(-45); xticklabels(octetLabels); yticks(octetTicks); yticklabels(octetLabels); ytickangle(45); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; saveas(gcf,'all-toxins-octet-scatter3.pdf')
scatter3(A(:,1),A(:,2),B,B,B,'filled'); axis ij; xticks(octetTicks); xtickangle(-45); xticklabels(octetLabels); yticks(octetTicks); yticklabels(octetLabels); ytickangle(45); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; saveas(gcf,'all-toxins-octet-scatter3-3dev.pdf')
stem3(A(:,1),A(:,2),A(:,3),'MarkerFaceColor','red','Color','k'); xticks(octetTicks); xtickangle(45); xticklabels(octetLabels); xtickangle(-45); yticks(octetTicks); yticklabels(octetLabels); ytickangle(45); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; axis ij; saveas(gcf,'all-toxins-octet-stem3.pdf')
stem3(A(:,1),A(:,2),B,'MarkerFaceColor','red','Color','k'); xticks(octetTicks); xtickangle(45); xticklabels(octetLabels); xtickangle(-45); yticks(octetTicks); yticklabels(octetLabels); ytickangle(45); grid on; ax = gca; ax.LineWidth = 1.0; ax.GridColor = 'black'; ax.GridAlpha = 0.9; axis ij; saveas(gcf,'all-toxins-octet-stem3-3dev.pdf')
