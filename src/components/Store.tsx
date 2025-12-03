import { ArrowLeft, Coins, ShoppingBag, Zap, TrendingUp, Star, Gift, Sparkles, Clock, Target, Award, Shield, Rocket } from 'lucide-react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { useState } from 'react';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from './ui/dialog';

interface StoreProps {
  userName: string;
  onBack: () => void;
}

interface StoreItem {
  id: string;
  name: string;
  description: string;
  price: number;
  icon: React.ElementType;
  category: 'booster' | 'upgrade' | 'special';
  duration?: string;
  benefit: string;
  popular?: boolean;
}

export function Store({ userName, onBack }: StoreProps) {
  const [userCoins, setUserCoins] = useState(850);
  const [selectedItem, setSelectedItem] = useState<StoreItem | null>(null);
  const [showPurchaseDialog, setShowPurchaseDialog] = useState(false);
  const [activeTab, setActiveTab] = useState<'all' | 'booster' | 'upgrade' | 'special'>('all');

  const storeItems: StoreItem[] = [
    {
      id: '1',
      name: 'Booster de Puntos 2x',
      description: 'Duplica tus puntos por cada reciclaje',
      price: 150,
      icon: Zap,
      category: 'booster',
      duration: '1 hora',
      benefit: '2x Puntos',
      popular: true
    },
    {
      id: '2',
      name: 'Booster de Puntos 3x',
      description: 'Triplica tus puntos por cada reciclaje',
      price: 300,
      icon: Rocket,
      category: 'booster',
      duration: '30 minutos',
      benefit: '3x Puntos'
    },
    {
      id: '3',
      name: 'Multiplicador de Racha',
      description: 'Aumenta tus puntos de racha diaria',
      price: 200,
      icon: TrendingUp,
      category: 'booster',
      duration: '24 horas',
      benefit: '+50% Racha',
      popular: true
    },
    {
      id: '4',
      name: 'Protector de Racha',
      description: 'Protege tu racha si olvidas reciclar un día',
      price: 250,
      icon: Shield,
      category: 'upgrade',
      duration: 'Permanente',
      benefit: '1 uso'
    },
    {
      id: '5',
      name: 'Búsqueda de Puntos Extra',
      description: 'Encuentra puntos ocultos en cada reciclaje',
      price: 100,
      icon: Target,
      category: 'booster',
      duration: '2 horas',
      benefit: '+20 pts/scan'
    },
    {
      id: '6',
      name: 'Caja de Recompensas',
      description: 'Recibe recompensas aleatorias de gran valor',
      price: 400,
      icon: Gift,
      category: 'special',
      benefit: 'Sorpresa',
      popular: true
    },
    {
      id: '7',
      name: 'Estrella de la Suerte',
      description: 'Aumenta las probabilidades de logros raros',
      price: 180,
      icon: Star,
      category: 'upgrade',
      duration: '7 días',
      benefit: '+30% Suerte'
    },
    {
      id: '8',
      name: 'Tiempo Extra',
      description: 'Extiende el tiempo de tus boosters activos',
      price: 120,
      icon: Clock,
      category: 'upgrade',
      duration: 'Permanente',
      benefit: '+15 min'
    },
    {
      id: '9',
      name: 'Pack de Inicio Diario',
      description: 'Gana bonos cada día que inicies sesión',
      price: 500,
      icon: Sparkles,
      category: 'special',
      duration: '30 días',
      benefit: 'Bonus Diario'
    },
    {
      id: '10',
      name: 'Insignia Premium',
      description: 'Muestra tu estatus premium en el ranking',
      price: 350,
      icon: Award,
      category: 'special',
      duration: 'Permanente',
      benefit: 'Exclusivo'
    }
  ];

  const handlePurchase = (item: StoreItem) => {
    setSelectedItem(item);
    setShowPurchaseDialog(true);
  };

  const confirmPurchase = () => {
    if (selectedItem && userCoins >= selectedItem.price) {
      setUserCoins(userCoins - selectedItem.price);
      setShowPurchaseDialog(false);
      // Aquí podrías agregar la lógica para aplicar el item comprado
    }
  };

  const getCategoryIcon = (category: StoreItem['category']) => {
    switch (category) {
      case 'booster': return Zap;
      case 'upgrade': return TrendingUp;
      case 'special': return Gift;
    }
  };

  const getCategoryColor = (category: StoreItem['category']) => {
    switch (category) {
      case 'booster': return 'from-orange-400 to-red-500';
      case 'upgrade': return 'from-blue-400 to-indigo-500';
      case 'special': return 'from-purple-400 to-pink-500';
    }
  };

  const getCategoryBadgeColor = (category: StoreItem['category']) => {
    switch (category) {
      case 'booster': return 'bg-orange-100 text-orange-700';
      case 'upgrade': return 'bg-blue-100 text-blue-700';
      case 'special': return 'bg-purple-100 text-purple-700';
    }
  };

  const getCategoryName = (category: StoreItem['category']) => {
    switch (category) {
      case 'booster': return 'Booster';
      case 'upgrade': return 'Mejora';
      case 'special': return 'Especial';
    }
  };

  const filteredItems = activeTab === 'all' 
    ? storeItems 
    : storeItems.filter(item => item.category === activeTab);

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 via-indigo-50 to-white pb-20">
      {/* Header */}
      <div className="bg-gradient-to-r from-blue-600 to-indigo-600 pt-12 pb-8 px-6 rounded-b-3xl shadow-lg mb-6">
        <div className="flex items-center mb-6">
          <button 
            onClick={onBack}
            className="bg-white/20 backdrop-blur-sm p-3 rounded-full hover:bg-white/30 transition-all shadow-md mr-4"
          >
            <ArrowLeft className="w-6 h-6 text-white" />
          </button>
          <div className="flex-1">
            <h1 className="text-white mb-1">Tienda Eco</h1>
            <p className="text-blue-100">Mejora tu experiencia de reciclaje</p>
          </div>
        </div>

        {/* Balance de monedas */}
        <Card className="bg-white/20 backdrop-blur-sm border-0 p-5">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="bg-yellow-400 rounded-full p-3 shadow-lg">
                <Coins className="w-6 h-6 text-yellow-900" />
              </div>
              <div>
                <p className="text-blue-100">Tus Monedas</p>
                <div className="text-white">{userCoins.toLocaleString()}</div>
              </div>
            </div>
            <Button 
              variant="outline" 
              className="bg-white/10 text-white border-white/30 hover:bg-white/20"
            >
              Obtener más
            </Button>
          </div>
        </Card>
      </div>

      {/* Filtros por categoría */}
      <div className="px-6 mb-6">
        <div className="flex gap-2 overflow-x-auto pb-2">
          <Button
            variant={activeTab === 'all' ? 'default' : 'outline'}
            onClick={() => setActiveTab('all')}
            className={activeTab === 'all' ? 'bg-blue-600 hover:bg-blue-700' : ''}
          >
            <ShoppingBag className="w-4 h-4 mr-2" />
            Todos
          </Button>
          <Button
            variant={activeTab === 'booster' ? 'default' : 'outline'}
            onClick={() => setActiveTab('booster')}
            className={activeTab === 'booster' ? 'bg-orange-600 hover:bg-orange-700' : ''}
          >
            <Zap className="w-4 h-4 mr-2" />
            Boosters
          </Button>
          <Button
            variant={activeTab === 'upgrade' ? 'default' : 'outline'}
            onClick={() => setActiveTab('upgrade')}
            className={activeTab === 'upgrade' ? 'bg-blue-600 hover:bg-blue-700' : ''}
          >
            <TrendingUp className="w-4 h-4 mr-2" />
            Mejoras
          </Button>
          <Button
            variant={activeTab === 'special' ? 'default' : 'outline'}
            onClick={() => setActiveTab('special')}
            className={activeTab === 'special' ? 'bg-purple-600 hover:bg-purple-700' : ''}
          >
            <Gift className="w-4 h-4 mr-2" />
            Especiales
          </Button>
        </div>
      </div>

      {/* Lista de items */}
      <div className="px-6 space-y-4">
        {filteredItems.map((item) => {
          const Icon = item.icon;
          const canAfford = userCoins >= item.price;
          
          return (
            <Card 
              key={item.id} 
              className={`border-0 shadow-md transition-all relative overflow-hidden ${
                !canAfford && 'opacity-60'
              }`}
            >
              {item.popular && (
                <div className="absolute top-0 right-0 bg-gradient-to-r from-yellow-400 to-orange-500 text-white px-4 py-1 rounded-bl-lg shadow-md">
                  <span className="flex items-center gap-1">
                    <Star className="w-3 h-3" />
                    Popular
                  </span>
                </div>
              )}
              
              <div className="p-5">
                <div className="flex items-start gap-4">
                  {/* Icono del item */}
                  <div className={`flex-shrink-0 w-16 h-16 rounded-xl bg-gradient-to-br ${getCategoryColor(item.category)} flex items-center justify-center shadow-lg`}>
                    <Icon className="w-8 h-8 text-white" />
                  </div>

                  {/* Información del item */}
                  <div className="flex-1 min-w-0">
                    <div className="flex items-start justify-between gap-2 mb-2">
                      <div className="flex-1 min-w-0">
                        <h3 className="text-gray-800 mb-1">{item.name}</h3>
                        <p className="text-gray-600">{item.description}</p>
                      </div>
                    </div>

                    {/* Categoría y beneficio */}
                    <div className="flex items-center gap-2 mb-3">
                      <Badge className={`${getCategoryBadgeColor(item.category)} border-0`}>
                        {getCategoryName(item.category)}
                      </Badge>
                      {item.duration && (
                        <Badge variant="outline" className="text-gray-600">
                          <Clock className="w-3 h-3 mr-1" />
                          {item.duration}
                        </Badge>
                      )}
                    </div>

                    {/* Beneficio destacado */}
                    <div className="bg-green-50 rounded-lg px-3 py-2 mb-3">
                      <div className="flex items-center gap-2">
                        <Sparkles className="w-4 h-4 text-green-600" />
                        <span className="text-green-700">{item.benefit}</span>
                      </div>
                    </div>

                    {/* Precio y botón de compra */}
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <Coins className="w-5 h-5 text-yellow-500" />
                        <span className="text-gray-800">{item.price}</span>
                      </div>
                      <Button
                        onClick={() => handlePurchase(item)}
                        disabled={!canAfford}
                        className="bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700"
                      >
                        {canAfford ? 'Comprar' : 'Sin fondos'}
                      </Button>
                    </div>
                  </div>
                </div>
              </div>
            </Card>
          );
        })}
      </div>

      {/* Mensaje promocional */}
      <div className="px-6 mt-8 mb-8">
        <Card className="bg-gradient-to-r from-green-50 to-emerald-50 border-0 p-5 shadow-md">
          <div className="flex items-start gap-3">
            <div className="bg-green-600 rounded-full p-2 flex-shrink-0">
              <Gift className="w-5 h-5 text-white" />
            </div>
            <div>
              <h3 className="text-gray-800 mb-2">¡Gana más monedas!</h3>
              <p className="text-gray-600">
                Recicla más elementos, completa logros y mantén tu racha para ganar monedas gratis cada día.
              </p>
            </div>
          </div>
        </Card>
      </div>

      {/* Dialog de confirmación de compra */}
      <Dialog open={showPurchaseDialog} onOpenChange={setShowPurchaseDialog}>
        <DialogContent className="max-w-sm">
          <DialogHeader>
            <DialogTitle>Confirmar Compra</DialogTitle>
            <DialogDescription>
              ¿Estás seguro de que quieres comprar este item?
            </DialogDescription>
          </DialogHeader>
          
          {selectedItem && (
            <div className="py-4">
              <div className="flex items-center gap-4 mb-4">
                {(() => {
                  const Icon = selectedItem.icon;
                  return (
                    <div className={`w-16 h-16 rounded-xl bg-gradient-to-br ${getCategoryColor(selectedItem.category)} flex items-center justify-center shadow-lg`}>
                      <Icon className="w-8 h-8 text-white" />
                    </div>
                  );
                })()}
                <div>
                  <h3 className="text-gray-800 mb-1">{selectedItem.name}</h3>
                  <div className="flex items-center gap-2">
                    <Coins className="w-4 h-4 text-yellow-500" />
                    <span className="text-gray-700">{selectedItem.price} monedas</span>
                  </div>
                </div>
              </div>
              
              <div className="bg-gray-50 rounded-lg p-3 mb-4">
                <div className="flex justify-between mb-2">
                  <span className="text-gray-600">Balance actual:</span>
                  <span className="text-gray-800">{userCoins} monedas</span>
                </div>
                <div className="flex justify-between mb-2">
                  <span className="text-gray-600">Costo:</span>
                  <span className="text-gray-800">-{selectedItem.price} monedas</span>
                </div>
                <div className="border-t border-gray-200 pt-2 mt-2 flex justify-between">
                  <span className="text-gray-700">Nuevo balance:</span>
                  <span className="text-green-600">{userCoins - selectedItem.price} monedas</span>
                </div>
              </div>
            </div>
          )}

          <DialogFooter>
            <Button 
              variant="outline" 
              onClick={() => setShowPurchaseDialog(false)}
            >
              Cancelar
            </Button>
            <Button 
              onClick={confirmPurchase}
              className="bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700"
            >
              Confirmar
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
