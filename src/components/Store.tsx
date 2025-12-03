import { ArrowLeft, Coins, ShoppingBag, Zap, TrendingUp, Star, Gift, Sparkles, Clock, Target, Award, Shield, Rocket, Leaf, Trophy } from 'lucide-react';
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
      case 'upgrade': return 'from-emerald-400 to-teal-500';
      case 'special': return 'from-purple-400 to-pink-500';
    }
  };

  const getCategoryBadgeColor = (category: StoreItem['category']) => {
    switch (category) {
      case 'booster': return 'bg-orange-100 text-orange-700';
      case 'upgrade': return 'bg-emerald-100 text-emerald-700';
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
    <div className="min-h-screen bg-gradient-to-br from-emerald-50 via-teal-50 to-cyan-50 pb-24 animate-fade-in">
      {/* Header mejorado con estilo verde */}
      <div className="relative bg-gradient-to-r from-emerald-600 via-green-600 to-teal-600 pt-12 pb-24 px-6 rounded-b-[2.5rem] shadow-2xl overflow-hidden">
        {/* Efectos de fondo animados */}
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 transform rotate-12 animate-float">
            <ShoppingBag className="w-32 h-32" />
          </div>
          <div className="absolute bottom-10 left-10 transform -rotate-12 animate-pulse-glow">
            <Coins className="w-24 h-24" />
          </div>
        </div>

        {/* Efectos de luz */}
        <div className="absolute inset-0 overflow-hidden">
          <div className="absolute top-0 right-0 w-64 h-64 bg-teal-400/30 rounded-full blur-3xl animate-float"></div>
          <div className="absolute bottom-0 left-0 w-48 h-48 bg-emerald-400/20 rounded-full blur-2xl animate-pulse-glow"></div>
        </div>

        <div className="relative z-10">
          <div className="flex items-center mb-6">
            <button 
              onClick={onBack}
              className="mr-4 p-3 bg-white/20 backdrop-blur-md rounded-2xl hover:bg-white/30 transition-all shadow-lg border border-white/30 group"
            >
              <ArrowLeft className="w-6 h-6 text-white group-hover:-translate-x-1 transition-transform" />
            </button>
            <div className="flex-1">
              <div className="flex items-center gap-3 mb-2">
                <div className="relative">
                  <div className="absolute inset-0 bg-yellow-400 rounded-full blur-xl opacity-60"></div>
                  <div className="relative bg-gradient-to-br from-yellow-400 to-amber-500 rounded-2xl p-2 shadow-xl">
                    <ShoppingBag className="w-8 h-8 text-white drop-shadow-lg" />
                  </div>
                </div>
                <div>
                  <h1 className="text-white drop-shadow-lg">Tienda Eco</h1>
                  <p className="text-emerald-100 drop-shadow-md">Mejora tu experiencia</p>
                </div>
              </div>
            </div>
          </div>

          {/* Balance de monedas mejorado */}
          <Card className="relative bg-white/20 backdrop-blur-md border-0 shadow-2xl rounded-3xl p-6 overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-r from-white/5 to-white/10"></div>
            <div className="relative z-10 flex items-center justify-between">
              <div className="flex items-center gap-4">
                <div className="relative">
                  <div className="absolute -inset-1 bg-gradient-to-r from-yellow-400 to-amber-500 rounded-full blur opacity-60 animate-pulse"></div>
                  <div className="relative bg-gradient-to-br from-yellow-400 to-amber-500 rounded-2xl p-3.5 shadow-2xl">
                    <Coins className="w-7 h-7 text-white drop-shadow-lg" />
                  </div>
                </div>
                <div>
                  <p className="text-emerald-100 mb-1">Tus Monedas</p>
                  <div className="flex items-center gap-2">
                    <span className="text-white text-3xl">{userCoins.toLocaleString()}</span>
                    <Sparkles className="w-5 h-5 text-yellow-300 animate-pulse" />
                  </div>
                </div>
              </div>
              <Button 
                className="bg-white/20 backdrop-blur-sm text-white border border-white/30 hover:bg-white/30 shadow-lg"
              >
                <Leaf className="w-4 h-4 mr-2" />
                Ganar más
              </Button>
            </div>
          </Card>
        </div>
      </div>

      {/* Filtros por categoría mejorados */}
      <div className="px-6 -mt-16 mb-6 relative z-10 animate-slide-up">
        <div className="flex gap-3 overflow-x-auto pb-2 scrollbar-hide">
          <Button
            variant={activeTab === 'all' ? 'default' : 'outline'}
            onClick={() => setActiveTab('all')}
            className={`rounded-2xl shadow-lg transition-all whitespace-nowrap ${
              activeTab === 'all' 
                ? 'bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-700 hover:to-teal-700 text-white border-0' 
                : 'bg-white hover:bg-emerald-50 hover:border-emerald-500'
            }`}
          >
            <ShoppingBag className="w-4 h-4 mr-2" />
            Todos
          </Button>
          <Button
            variant={activeTab === 'booster' ? 'default' : 'outline'}
            onClick={() => setActiveTab('booster')}
            className={`rounded-2xl shadow-lg transition-all whitespace-nowrap ${
              activeTab === 'booster' 
                ? 'bg-gradient-to-r from-orange-500 to-red-500 hover:from-orange-600 hover:to-red-600 text-white border-0' 
                : 'bg-white hover:bg-orange-50 hover:border-orange-500'
            }`}
          >
            <Zap className="w-4 h-4 mr-2" />
            Boosters
          </Button>
          <Button
            variant={activeTab === 'upgrade' ? 'default' : 'outline'}
            onClick={() => setActiveTab('upgrade')}
            className={`rounded-2xl shadow-lg transition-all whitespace-nowrap ${
              activeTab === 'upgrade' 
                ? 'bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-white border-0' 
                : 'bg-white hover:bg-emerald-50 hover:border-emerald-500'
            }`}
          >
            <TrendingUp className="w-4 h-4 mr-2" />
            Mejoras
          </Button>
          <Button
            variant={activeTab === 'special' ? 'default' : 'outline'}
            onClick={() => setActiveTab('special')}
            className={`rounded-2xl shadow-lg transition-all whitespace-nowrap ${
              activeTab === 'special' 
                ? 'bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white border-0' 
                : 'bg-white hover:bg-purple-50 hover:border-purple-500'
            }`}
          >
            <Gift className="w-4 h-4 mr-2" />
            Especiales
          </Button>
        </div>
      </div>

      {/* Lista de items mejorada */}
      <div className="px-6 space-y-4">
        {filteredItems.map((item) => {
          const Icon = item.icon;
          const canAfford = userCoins >= item.price;
          
          return (
            <Card 
              key={item.id} 
              className={`border-0 shadow-xl hover:shadow-2xl transition-all relative overflow-hidden bg-white rounded-3xl group ${
                !canAfford && 'opacity-60'
              }`}
            >
              {/* Efecto de brillo en hover */}
              <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity"></div>

              {item.popular && (
                <div className="absolute top-0 right-0 bg-gradient-to-r from-yellow-400 via-amber-500 to-orange-500 text-white px-5 py-2 rounded-bl-2xl shadow-lg z-10">
                  <span className="flex items-center gap-1.5">
                    <Star className="w-4 h-4 fill-current animate-pulse" />
                    Popular
                  </span>
                </div>
              )}
              
              <div className="p-6">
                <div className="flex items-start gap-5">
                  {/* Icono del item mejorado */}
                  <div className="relative flex-shrink-0">
                    <div className={`absolute -inset-1 bg-gradient-to-br ${getCategoryColor(item.category)} rounded-2xl blur opacity-40 group-hover:opacity-60 transition-opacity`}></div>
                    <div className={`relative w-20 h-20 rounded-2xl bg-gradient-to-br ${getCategoryColor(item.category)} flex items-center justify-center shadow-2xl group-hover:scale-105 transition-transform`}>
                      <Icon className="w-10 h-10 text-white drop-shadow-lg" />
                    </div>
                  </div>

                  {/* Información del item */}
                  <div className="flex-1 min-w-0">
                    <div className="mb-3">
                      <h3 className="text-gray-800 mb-1.5">{item.name}</h3>
                      <p className="text-gray-600">{item.description}</p>
                    </div>

                    {/* Categoría y duración */}
                    <div className="flex items-center gap-2 mb-4">
                      <Badge className={`${getCategoryBadgeColor(item.category)} border-0 shadow-sm`}>
                        {getCategoryName(item.category)}
                      </Badge>
                      {item.duration && (
                        <Badge className="bg-gray-100 text-gray-700 border-0 shadow-sm">
                          <Clock className="w-3 h-3 mr-1.5" />
                          {item.duration}
                        </Badge>
                      )}
                    </div>

                    {/* Beneficio destacado */}
                    <div className="bg-gradient-to-r from-emerald-50 to-green-50 rounded-xl px-4 py-3 mb-4 border border-emerald-100">
                      <div className="flex items-center gap-2">
                        <div className="bg-emerald-500 rounded-lg p-1.5">
                          <Sparkles className="w-4 h-4 text-white" />
                        </div>
                        <span className="text-emerald-700">{item.benefit}</span>
                      </div>
                    </div>

                    {/* Precio y botón de compra */}
                    <div className="flex items-center justify-between gap-3">
                      <div className="flex items-center gap-2 bg-yellow-50 rounded-xl px-4 py-2.5 border border-yellow-200">
                        <Coins className="w-5 h-5 text-yellow-600" />
                        <span className="text-gray-800">{item.price}</span>
                      </div>
                      <Button
                        onClick={() => handlePurchase(item)}
                        disabled={!canAfford}
                        className={`rounded-xl shadow-lg transition-all px-6 ${
                          canAfford 
                            ? 'bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-700 hover:to-teal-700 hover:shadow-xl hover:scale-105' 
                            : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                        }`}
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

      {/* Mensaje promocional mejorado */}
      <div className="px-6 mt-8 mb-8">
        <Card className="relative bg-gradient-to-br from-emerald-100 via-green-50 to-teal-100 border-0 shadow-xl rounded-3xl overflow-hidden">
          <div className="absolute top-0 right-0 w-32 h-32 bg-emerald-300/30 rounded-full blur-2xl"></div>
          <div className="absolute bottom-0 left-0 w-24 h-24 bg-teal-300/20 rounded-full blur-xl"></div>
          
          <div className="relative p-6">
            <div className="flex items-start gap-4">
              <div className="relative flex-shrink-0">
                <div className="absolute -inset-1 bg-gradient-to-br from-emerald-500 to-teal-600 rounded-2xl blur opacity-50"></div>
                <div className="relative bg-gradient-to-br from-emerald-600 to-teal-700 rounded-2xl p-3 shadow-xl">
                  <Gift className="w-6 h-6 text-white drop-shadow-lg" />
                </div>
              </div>
              <div className="flex-1">
                <h3 className="text-gray-800 mb-2">¡Gana más monedas!</h3>
                <p className="text-gray-600 mb-4">
                  Recicla más elementos, completa logros y mantén tu racha para ganar monedas gratis cada día.
                </p>
                <div className="flex items-center gap-2">
                  <div className="flex items-center gap-1 bg-white rounded-lg px-3 py-1.5 shadow-sm">
                    <Leaf className="w-4 h-4 text-green-600" />
                    <span className="text-sm text-gray-700">+50 monedas/día</span>
                  </div>
                  <div className="flex items-center gap-1 bg-white rounded-lg px-3 py-1.5 shadow-sm">
                    <Trophy className="w-4 h-4 text-yellow-600" />
                    <span className="text-sm text-gray-700">+100 por logro</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </Card>
      </div>

      {/* Dialog de confirmación de compra mejorado */}
      <Dialog open={showPurchaseDialog} onOpenChange={setShowPurchaseDialog}>
        <DialogContent className="max-w-sm rounded-3xl border-0 shadow-2xl">
          <DialogHeader>
            <DialogTitle className="text-gray-800">Confirmar Compra</DialogTitle>
            <DialogDescription className="text-gray-600">
              ¿Estás seguro de que quieres comprar este item?
            </DialogDescription>
          </DialogHeader>
          
          {selectedItem && (
            <div className="py-4">
              <div className="flex items-center gap-4 mb-6">
                {(() => {
                  const Icon = selectedItem.icon;
                  return (
                    <div className="relative">
                      <div className={`absolute -inset-1 bg-gradient-to-br ${getCategoryColor(selectedItem.category)} rounded-2xl blur opacity-50`}></div>
                      <div className={`relative w-20 h-20 rounded-2xl bg-gradient-to-br ${getCategoryColor(selectedItem.category)} flex items-center justify-center shadow-2xl`}>
                        <Icon className="w-10 h-10 text-white drop-shadow-lg" />
                      </div>
                    </div>
                  );
                })()}
                <div className="flex-1">
                  <h3 className="text-gray-800 mb-2">{selectedItem.name}</h3>
                  <div className="flex items-center gap-2 bg-yellow-50 rounded-lg px-3 py-2 border border-yellow-200">
                    <Coins className="w-5 h-5 text-yellow-600" />
                    <span className="text-gray-700">{selectedItem.price} monedas</span>
                  </div>
                </div>
              </div>
              
              <div className="bg-gradient-to-br from-gray-50 to-gray-100 rounded-2xl p-4 mb-4 border border-gray-200">
                <div className="flex justify-between mb-3">
                  <span className="text-gray-600">Balance actual:</span>
                  <div className="flex items-center gap-1.5">
                    <Coins className="w-4 h-4 text-yellow-600" />
                    <span className="text-gray-800">{userCoins}</span>
                  </div>
                </div>
                <div className="flex justify-between mb-3">
                  <span className="text-gray-600">Costo:</span>
                  <div className="flex items-center gap-1.5">
                    <Coins className="w-4 h-4 text-red-500" />
                    <span className="text-red-600">-{selectedItem.price}</span>
                  </div>
                </div>
                <div className="border-t border-gray-300 pt-3 mt-3 flex justify-between">
                  <span className="text-gray-700">Nuevo balance:</span>
                  <div className="flex items-center gap-1.5">
                    <Coins className="w-5 h-5 text-emerald-600" />
                    <span className="text-emerald-600">{userCoins - selectedItem.price}</span>
                  </div>
                </div>
              </div>
            </div>
          )}

          <DialogFooter className="gap-2">
            <Button 
              variant="outline" 
              onClick={() => setShowPurchaseDialog(false)}
              className="rounded-xl"
            >
              Cancelar
            </Button>
            <Button 
              onClick={confirmPurchase}
              className="bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-700 hover:to-teal-700 rounded-xl shadow-lg"
            >
              Confirmar Compra
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
