export default function Diets(props) {
  let { vegetarian, vegan, glutenFree, dairyFree } = props.recipe;
  let symbolSize = props.symbolSize;

  return (
    <div className="diet">
      {vegetarian && (
        <img
          src="/vegetarian.png"
          alt="vegetarian"
          title="vegetarian"
          height={symbolSize}
        />
      )}
      {vegan && (
        <img src="/vegan.png" alt="vegan" title="vegan" height={symbolSize} />
      )}
      {glutenFree && (
        <img
          src="/gluten-free.png"
          alt="gluten-free"
          title="gluten free"
          height={symbolSize}
        />
      )}
      {dairyFree && (
        <img
          src="/dairy-free.png"
          alt="dairy-free"
          title="dairy free"
          height={symbolSize}
        />
      )}
    </div>
  );
}
