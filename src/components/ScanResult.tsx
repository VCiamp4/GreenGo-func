import { CheckCircle2, Coins, Leaf, X, Share2 } from 'lucide-react';
import { Button } from './ui/button';
import { Card, CardContent } from './ui/card';
import { Badge } from './ui/badge';

interface ScanResultProps {
  qrData: string;
  onClose: () => void;
  onContinue: () => void;
}

export function ScanResult({ qrData, onClose, onContinue }: ScanResultProps) {
  // Simular datos del material reciclado
  const recycleData = {
    type: 'Plástico PET',
    weight: '0.5 kg',
    points: 50,
    co2Saved: '1.2 kg',
    category: 'Botellas'
  };

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <Card className="w-full max-w-md border-0 shadow-2xl animate-[slide-up_0.3s_ease-out]">
        <CardContent className="p-0">
          {/* Header con éxito */}
          <div className="bg-gradient-to-br from-green-500 to-emerald-600 p-8 text-center relative">
            <button 
              onClick={onClose}
              className="absolute top-4 right-4 p-2 bg-white/20 backdrop-blur-sm rounded-full hover:bg-white/30 transition-all"
            >
              <X className="w-5 h-5 text-white" />
            </button>
            
            <div className="inline-flex items-center justify-center w-20 h-20 bg-white/20 backdrop-blur-sm rounded-full mb-4">
              <CheckCircle2 className="w-12 h-12 text-white" />
            </div>
            <h2 className="text-white mb-2">¡Reciclaje Exitoso!</h2>
            <p className="text-green-50">Has contribuido al cuidado del planeta</p>
          </div>

          {/* Contenido */}
          <div className="p-6 space-y-6">
            {/* Puntos ganados */}
            <div className="text-center">
              <div className="inline-flex items-center gap-2 bg-gradient-to-r from-yellow-400 to-amber-500 text-white px-6 py-3 rounded-full shadow-lg">
                <Coins className="w-6 h-6" />
                <span className="text-2xl">+{recycleData.points}</span>
                <span>puntos</span>
              </div>
            </div>

            {/* Detalles del reciclaje */}
            <div className="space-y-3">
              <div className="flex items-center justify-between p-4 bg-gray-50 rounded-xl">
                <span className="text-gray-600">Material</span>
                <span className="text-gray-900">{recycleData.type}</span>
              </div>
              
              <div className="flex items-center justify-between p-4 bg-gray-50 rounded-xl">
                <span className="text-gray-600">Categoría</span>
                <Badge className="bg-blue-100 text-blue-700 hover:bg-blue-100">
                  {recycleData.category}
                </Badge>
              </div>

              <div className="flex items-center justify-between p-4 bg-gray-50 rounded-xl">
                <span className="text-gray-600">Peso</span>
                <span className="text-gray-900">{recycleData.weight}</span>
              </div>
            </div>

            {/* Impacto ambiental */}
            <div className="bg-gradient-to-br from-green-50 to-emerald-50 p-4 rounded-xl border border-green-200">
              <div className="flex items-center gap-3 mb-2">
                <div className="p-2 bg-green-500 rounded-lg">
                  <Leaf className="w-5 h-5 text-white" />
                </div>
                <div>
                  <p className="text-gray-900">Impacto Positivo</p>
                  <p className="text-gray-600">CO₂ evitado: {recycleData.co2Saved}</p>
                </div>
              </div>
            </div>

            {/* QR Code Info */}
            <div className="text-center">
              <p className="text-gray-400">Código: {qrData}</p>
            </div>

            {/* Botones de acción */}
            <div className="space-y-3">
              <Button 
                onClick={onContinue}
                className="w-full bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-700 hover:to-emerald-700"
              >
                Continuar
              </Button>
              <Button 
                variant="outline"
                className="w-full"
              >
                <Share2 className="w-4 h-4 mr-2" />
                Compartir mi logro
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Animación */}
      <style>{`
        @keyframes slide-up {
          0% { transform: translateY(100px); opacity: 0; }
          100% { transform: translateY(0); opacity: 1; }
        }
      `}</style>
    </div>
  );
}
