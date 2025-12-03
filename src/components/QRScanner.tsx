import { useState, useEffect } from 'react';
import { X, Flashlight, ImageIcon, Zap, CheckCircle2 } from 'lucide-react';
import { Button } from './ui/button';
import { Card, CardContent } from './ui/card';

interface QRScannerProps {
  onClose: () => void;
  onScanSuccess: (data: string) => void;
}

export function QRScanner({ onClose, onScanSuccess }: QRScannerProps) {
  const [isScanning, setIsScanning] = useState(true);
  const [flashOn, setFlashOn] = useState(false);
  const [scanSuccess, setScanSuccess] = useState(false);

  // Simular escaneo exitoso después de 3 segundos
  useEffect(() => {
    const timer = setTimeout(() => {
      handleScanSuccess();
    }, 3000);

    return () => clearTimeout(timer);
  }, []);

  const handleScanSuccess = () => {
    setIsScanning(false);
    setScanSuccess(true);
    
    // Simular datos del QR escaneado
    setTimeout(() => {
      onScanSuccess('RECYCLE-PLASTIC-001');
    }, 1500);
  };

  return (
    <div className="fixed inset-0 bg-black z-50 flex flex-col">
      {/* Header */}
      <div className="relative bg-gradient-to-b from-black/80 to-transparent p-6 z-10">
        <div className="flex items-center justify-between max-w-md mx-auto">
          <h2 className="text-white">Escanear QR</h2>
          <button 
            onClick={onClose}
            className="p-2 bg-white/10 backdrop-blur-sm rounded-full hover:bg-white/20 transition-all"
          >
            <X className="w-6 h-6 text-white" />
          </button>
        </div>
      </div>

      {/* Área de escaneo */}
      <div className="flex-1 relative flex items-center justify-center">
        {/* Simulación de vista de cámara */}
        <div className="absolute inset-0 bg-gradient-to-b from-gray-900 via-gray-800 to-gray-900">
          {/* Efecto de "ruido" de cámara */}
          <div className="absolute inset-0 opacity-10">
            <div className="w-full h-full bg-[url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI1IiBoZWlnaHQ9IjUiPgo8cmVjdCB3aWR0aD0iNSIgaGVpZ2h0PSI1IiBmaWxsPSIjMDAwIj48L3JlY3Q+CjxwYXRoIGQ9Ik0wIDVMNSAwWk02IDRMNCA2Wk0tMSAxTDEgLTFaIiBzdHJva2U9IiM4ODgiIHN0cm9rZS13aWR0aD0iMSI+PC9wYXRoPgo8L3N2Zz4=')]"></div>
          </div>
        </div>

        {/* Marco de escaneo */}
        <div className="relative z-10 w-72 h-72">
          {/* Overlay oscuro con agujero en el centro */}
          <div className="absolute inset-0 -m-32">
            <div className="absolute inset-0 bg-black/60"></div>
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="w-72 h-72 shadow-[0_0_0_9999px_rgba(0,0,0,0.6)]"></div>
            </div>
          </div>

          {/* Marco con esquinas */}
          <div className="relative w-full h-full">
            {/* Esquina superior izquierda */}
            <div className="absolute top-0 left-0 w-12 h-12 border-l-4 border-t-4 border-green-500 rounded-tl-2xl"></div>
            {/* Esquina superior derecha */}
            <div className="absolute top-0 right-0 w-12 h-12 border-r-4 border-t-4 border-green-500 rounded-tr-2xl"></div>
            {/* Esquina inferior izquierda */}
            <div className="absolute bottom-0 left-0 w-12 h-12 border-l-4 border-b-4 border-green-500 rounded-bl-2xl"></div>
            {/* Esquina inferior derecha */}
            <div className="absolute bottom-0 right-0 w-12 h-12 border-r-4 border-b-4 border-green-500 rounded-br-2xl"></div>

            {/* Línea de escaneo animada */}
            {isScanning && !scanSuccess && (
              <div className="absolute inset-x-0 top-0 h-1 bg-gradient-to-r from-transparent via-green-400 to-transparent animate-[scan_2s_ease-in-out_infinite]"></div>
            )}

            {/* Indicador de éxito */}
            {scanSuccess && (
              <div className="absolute inset-0 flex items-center justify-center">
                <div className="bg-green-500/20 backdrop-blur-sm rounded-full p-6 animate-[scale-in_0.3s_ease-out]">
                  <CheckCircle2 className="w-16 h-16 text-green-400" />
                </div>
              </div>
            )}
          </div>

          {/* Indicador de estado */}
          {scanSuccess && (
            <div className="absolute -bottom-16 inset-x-0 text-center">
              <div className="inline-flex items-center gap-2 bg-green-500/20 backdrop-blur-sm px-4 py-2 rounded-full">
                <Zap className="w-4 h-4 text-green-400" />
                <span className="text-green-400">¡QR Detectado!</span>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Instrucciones y controles */}
      <div className="relative bg-gradient-to-t from-black/80 to-transparent p-6 z-10">
        <div className="max-w-md mx-auto space-y-4">
          {/* Instrucciones */}
          <div className="text-center text-white mb-6">
            <p className="mb-2">
              {isScanning && !scanSuccess ? 'Apunta la cámara al código QR' : ''}
            </p>
            <p className="text-gray-400">
              Asegúrate de que el código esté dentro del marco
            </p>
          </div>

          {/* Botones de control */}
          <div className="flex gap-4 justify-center">
            <button
              onClick={() => setFlashOn(!flashOn)}
              className={`p-4 rounded-full backdrop-blur-sm transition-all ${
                flashOn 
                  ? 'bg-yellow-500/30 text-yellow-400' 
                  : 'bg-white/10 text-white hover:bg-white/20'
              }`}
            >
              <Flashlight className="w-6 h-6" />
            </button>
            <button
              className="p-4 bg-white/10 backdrop-blur-sm rounded-full hover:bg-white/20 transition-all"
            >
              <ImageIcon className="w-6 h-6 text-white" />
            </button>
          </div>
        </div>
      </div>

      {/* Estilos para animaciones personalizadas */}
      <style>{`
        @keyframes scan {
          0%, 100% { transform: translateY(0); }
          50% { transform: translateY(288px); }
        }
        @keyframes scale-in {
          0% { transform: scale(0); opacity: 0; }
          50% { transform: scale(1.2); }
          100% { transform: scale(1); opacity: 1; }
        }
      `}</style>
    </div>
  );
}
